package cn.tianzy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GifConfig {
    @Id
    @GeneratedValue
    private Integer gifId;
    private String gifName;
    private String defaultSentences;
    private String previewImg;
    private Date addTime;
    private String addUser;
}
