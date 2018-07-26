package cn.tianzy.controller;

import cn.tianzy.utils.PinYinUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import cn.tianzy.entity.Subtitles;
import cn.tianzy.model.SentenceInfo;
import cn.tianzy.service.GifService;
import cn.tianzy.service.QcloudService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by lizhihao on 2018/3/11.
 */
@RestController
@RequestMapping(path = "/gif")
@Slf4j
public class GifController {

    @Autowired
    GifService gifService;

    @Autowired
    QcloudService qcloudService;

    @ApiOperation(value = "生成gif图片,并返回地址", notes = "")
    @RequestMapping(path = "/filePath", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map renderGifPath(@RequestBody Subtitles subtitles){
        ConcurrentMap<String, String> map = Maps.newConcurrentMap();
        try{
            String gifPath = gifService.renderGif(subtitles);
            map.put("code", "0");
            map.put("result", gifPath);
        } catch (Exception e) {
            map.put("code", "1");
            map.put("result", "");
            log.error("", e);
        }
        return map;
    }

    @ApiOperation(value = "获取gif", notes = "")
    @RequestMapping(path = "/file", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.MULTIPART_FORM_DATA_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<Resource> renderGif(@RequestBody Subtitles subtitles) throws Exception {
        String file = gifService.renderGif(subtitles);
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=txtx.gif").body(resource);
    }

    @RequestMapping(value = "/makeGif", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.TEXT_HTML_VALUE)
    public String makeGif(MultipartFile video, MultipartFile previewImg, int gifId, String gifName,String defaultSentences, String sentenceInfos){
        Map<String, Object> rsMap = new LinkedHashMap<>();
        rsMap.put("status", -1);
        try {
            if(video==null || video.getOriginalFilename().equals("")) {
                rsMap.put("errorMsg","视频文件不能为空");
                return JSONObject.toJSONString(rsMap);
            }
            if(previewImg==null || previewImg.getOriginalFilename().equals("")) {
                rsMap.put("errorMsg","图片不能为空");
                return JSONObject.toJSONString(rsMap);
            }
            if(StringUtils.isBlank(gifName)) {
                rsMap.put("errorMsg","gif名称不能为空");
                return JSONObject.toJSONString(rsMap);
            }

            gifId = gifService.makeGif(video, previewImg, gifId, gifName, defaultSentences, sentenceInfos);

            rsMap.put("status", 200);
            rsMap.put("gifId", gifId);
        } catch (Exception e) {
            log.error("", e);
            rsMap.put("status", -1);
            rsMap.put("errorMsg","操作异常");
        }
        return JSONObject.toJSONString(rsMap);
    }

    @RequestMapping("/delGif")
    public String delGif(int gifId){

        gifService.delGif(gifId);

        return "ok";
    }


}
