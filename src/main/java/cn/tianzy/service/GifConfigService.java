package cn.tianzy.service;

import cn.tianzy.dao.GifConfigRepository;
import cn.tianzy.entity.GifConfig;
import cn.tianzy.model.GifConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GifConfigService {
    @Autowired
    GifConfigRepository gifConfigRepository;

    public GifConfig save(GifConfig gifConfig ){
        return gifConfigRepository.save(gifConfig);
    }


    public GifConfig getByName(String name){
        GifConfig gifList = gifConfigRepository.findTopByGifName(name);
        return gifList;
    }

    public List<GifConfigModel> getGifConfigList(){
        List<GifConfig> gifConfigList = gifConfigRepository.findAll();

        return gifConfigList.stream().map(gifConfig -> GifConfigModel.newInstance(gifConfig)).collect(Collectors.toList());
    }
}
