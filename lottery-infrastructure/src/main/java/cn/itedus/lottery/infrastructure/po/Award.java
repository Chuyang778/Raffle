package cn.itedus.lottery.infrastructure.po;

import lombok.Data;

import java.util.Date;

/**
 * @author ChuYang
 * @version 1.0
 */
@Data
public class Award {
    private Long id;
    private String awardId;
    private Integer awardType;

    private Integer awardCount;

    private String awardName;

    // 奖品内容「文字描述、Key、码」
    private String awardContent;

    // 创建时间
    private Date createTime;

    // 修改时间
    private Date updateTime;

}
