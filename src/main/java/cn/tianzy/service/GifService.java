package cn.tianzy.service;

import cn.tianzy.utils.PinYinUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import cn.tianzy.dao.GifConfigRepository;
import cn.tianzy.entity.GifConfig;
import cn.tianzy.entity.Subtitles;
import cn.tianzy.model.SentenceInfo;
import cn.tianzy.utils.MyFileUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by lizhihao on 2018/3/11.
 */
@Service
@Getter
@Setter
public class GifService {

    private static final Logger logger = LoggerFactory.getLogger(GifService.class);

    @Value("${gif.work.dir}")
    private String gifWorkDir;

    /**
     * 字幕模板文件目录
     */
    @Value("${gif.temp.path}")
    private String tempPath;

    /**
     * gif生成目录
     */
    @Value("${gif.public.dir}")
    private String publicPath;

    @Autowired
    GifConfigRepository gifConfigRepository;

    public String getGifDir(int gifID, String gifName){
        return gifID + "-" + PinYinUtils.getPinYin(gifName);
    }

    public String getGifDir(Subtitles subtitles){
        return getGifDir(subtitles.getGifId(), subtitles.getTemplateName());
    }

    public String renderGif(Subtitles subtitles) {
        String assRelativePath = renderAss(subtitles);
        String gifDir = getGifDir(subtitles);

        String gifRelativePath = gifDir + "/" + UUID.randomUUID() + ".gif";
        String gifPath = publicPath + gifRelativePath;
        MyFileUtils.mkParentDir(gifPath);

        String videoPath = tempPath + gifDir + "/template.mp4";

        String scale = "300:-1";
        if ("simple".equals(subtitles.getMode())) {
            scale = "180:-1";
        }
        String cmd = String.format(" ffmpeg -i %s -r 6 -vf ass=%s,scale=%s -y %s", videoPath, assRelativePath, scale, gifPath);
        logger.info("cmd: {}", cmd);
        try {
            Process exec = Runtime.getRuntime().exec(cmd, null, new File(gifWorkDir));

            InputStream is = exec.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
            String line;
            while ( (line=br.readLine())!=null ){
//                logger.info("输出====="+line);
                System.out.println("输出====="+line);
            }
//            logger.info("输出:{}", IOUtils.toString(exec.getInputStream()));
            exec.waitFor();
        } catch (Exception e) {
            throw new RuntimeException("生成gif报错：{}", e);
        }
        return gifRelativePath;
    }



    /**
     * 根据模板文件，生成字幕ass文件
     * @param subtitles
     * @return ass文件在工作目录的相对路径
     * @throws Exception
     */
    private String renderAss(Subtitles subtitles) {
        String assRelativePath = "tmp/" + getGifDir(subtitles) + "/" + UUID.randomUUID().toString().replace("-", "") + ".ass";
        String assPath = gifWorkDir + "/" + assRelativePath;
        MyFileUtils.mkParentDir(assPath);
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setDirectoryForTemplateLoading(Paths.get(tempPath).resolve(getGifDir(subtitles)).toFile());
            Map<String, Object> root = new HashMap<>();
            Map<String, String> mx = new HashMap<>();
            List<String> list = Splitter.on(",").splitToList(subtitles.getSentence());
            for (int i = 0; i < list.size(); i++) {
                mx.put("sentences" + i, list.get(i));
            }
            root.put("mx", mx);

//            try (FileWriter writer = new FileWriter(assPath)) {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(assPath), "UTF-8");) {
                Template temp = cfg.getTemplate("template.ftl");
                temp.process(root, writer);
            } catch (Exception e) {
                throw new RuntimeException("生成ass文件报错", e);
            }
        } catch (IOException e) {
            throw new RuntimeException("生成ass文件报错", e);
        }
        return assRelativePath;
    }

    public int makeGif(MultipartFile video, MultipartFile previewImg,int gifId, String gifName, String defaultSentences, String sentenceInfos) {

        //添加配置到数据库
        GifConfig gifConfig = addGigConfig(gifId, gifName, defaultSentences);


        //该gif的模板/预览图保存目录
        String gifDirName = getGifDir(gifConfig.getGifId(), gifName);

        //上传预览图
        String imgUrl = uploadImgFile(previewImg, gifDirName);
        gifConfig.setPreviewImg(imgUrl);
        //保存预览图到数据库
        gifConfigRepository.save(gifConfig);

        //生成字幕模板
        List<SentenceInfo> sentenceInfoList = (List<SentenceInfo>) JSONObject.parse(sentenceInfos);
        generateAssFtlFile(sentenceInfoList, gifDirName);

        //上传视频文件
        uploadVideoFile(video, gifDirName);

        return gifConfig.getGifId();
    }

    /**
     * 生成字幕模板文件
     * @param sentenceInfos
     * @param gifDir
     */
    public void generateAssFtlFile(List<SentenceInfo> sentenceInfos, String gifDir){
            String totalFtlPath = tempPath + "totalTemplate.ftl";
            String targetFtlPath = tempPath + gifDir + "/template.ftl";
            MyFileUtils.mkParentDir(targetFtlPath);

            Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
            cfg.setDefaultEncoding("UTF-8");
            Map<String, Object> root = new HashMap<>();
            root.put("sentences", sentenceInfos);

//            try (FileWriter writer = new FileWriter(targetFtlPath)) {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(targetFtlPath), "UTF-8");) {

                cfg.setDirectoryForTemplateLoading(new File(totalFtlPath).getParentFile());
                Template temp = cfg.getTemplate("totalTemplate.ftl");
                temp.process(root, writer);
            } catch (Exception e) {
                throw new RuntimeException("生成ass文件报错", e);
            }
    }

    /**
     * 视频文件上传
     * @param file
     */
    public void uploadVideoFile(MultipartFile file, String gifName){
        String videoPath = tempPath + gifName + "/template.mp4";
        MyFileUtils.mkParentDir(videoPath);
        uploadFile(file, new File(videoPath));
    }

    /**
     * 预览图上传
     * @param file
     * @param gifName
     * @return
     */
    public String uploadImgFile(MultipartFile file, String gifName){
        String fileName = gifName + MyFileUtils.getFileExtensionName(file.getOriginalFilename());
        String imgFilePath = publicPath + fileName;
        MyFileUtils.mkParentDir(imgFilePath);
        uploadFile(file, new File(imgFilePath));
        return "/"+fileName;
    }

    public GifConfig addGigConfig(int gifId, String gifName, String defaultSentences){
        GifConfig gifConfig = GifConfig.builder()
                .gifName(gifName)
                .defaultSentences(defaultSentences)
                .previewImg("")
                .addTime(new Date())
                .addUser("")
                .build();
        if(gifId>0){
            gifConfig.setGifId(gifId);
        }
        return gifConfigRepository.save(gifConfig);
    }

    private void uploadFile(MultipartFile srcFile, File targetFile) {
        try{
            srcFile.transferTo(targetFile);
        } catch (IOException e) {
            throw new RuntimeException("上传文件异常", e);
        }
    }

    public void delGif(int gifId) {
        GifConfig gifConfig = gifConfigRepository.findOne(gifId);

        //删除数据库配置
        gifConfigRepository.delete(gifId);

        //删除模板
        String dirName = getGifDir(gifId, gifConfig.getGifName());

        String tempDir = tempPath + dirName;
        String publicDir = publicPath + dirName;


        try {
            FileUtils.deleteDirectory(new File(tempDir));
            FileUtils.deleteDirectory(new File(publicDir));
            if(gifConfig.getPreviewImg().startsWith("/")){
                String previewImgUrl = publicPath + gifConfig.getPreviewImg();
                FileUtils.forceDelete(new File(previewImgUrl));
            }
        } catch (IOException e) {
            logger.error("", e);
        }

    }
}
