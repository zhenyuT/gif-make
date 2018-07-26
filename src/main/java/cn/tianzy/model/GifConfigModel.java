package cn.tianzy.model;

import cn.tianzy.entity.GifConfig;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class GifConfigModel {
    private GifConfig gifConfig;
    private List<String> defaultSentences = new ArrayList<>();

    public static GifConfigModel newInstance(GifConfig gifConfig) {
        GifConfigModel model = new GifConfigModel();
        model.gifConfig = gifConfig;
        if(!StringUtils.isBlank(gifConfig.getDefaultSentences())){
            model.defaultSentences = (List<String>) JSONObject.parse(gifConfig.getDefaultSentences());
        }
        return model;
    }
}
