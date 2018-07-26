package cn.tianzy;

import cn.tianzy.service.GifConfigService;
import com.alibaba.fastjson.JSONObject;
import cn.tianzy.entity.GifConfig;
import cn.tianzy.model.GifConfigModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GifConfigServiceTest {
    @Autowired
    GifConfigService gifConfigService;

    @Test
    public void save(){
        List<String> sentences = new ArrayList<>();
        sentences.add("迟迟");
        sentences.add("mu~~a");
        sentences.add("这么多人 怪不好意思");

        GifConfig gifConfig = GifConfig.builder()
                .gifName("chichi2")
                .defaultSentences(JSONObject.toJSONString(sentences))
                .previewImg("")
                .addTime(new Date())
                .addUser("tianzy")
                .build();
        GifConfig gifConfig1 = gifConfigService.save(gifConfig);
        System.out.println(gifConfig.getGifId());
    }

    @Test
    public void selectByName(){
        GifConfig gifList = gifConfigService.getByName("chichi");
        System.out.println(JSONObject.toJSONString(gifList));
        GifConfigModel model = GifConfigModel.newInstance(gifList);
        System.out.println(JSONObject.toJSONString(model));
    }

}
