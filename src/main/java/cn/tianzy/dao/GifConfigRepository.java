package cn.tianzy.dao;

import cn.tianzy.entity.GifConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GifConfigRepository extends JpaRepository<GifConfig, Integer> {
    GifConfig findTopByGifName(String name);
}
