package cn.itedus.lottery.infrastructure.po;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ChuYang
 * @version 1.0
 */
@Data
public class StrategyDetail {
    private String id;

    // 策略ID
    private Long strategyId;

    // 奖品ID
    private String awardId;

    // 奖品数量
    private String awardCount;
    /**
     * 奖品名称
     */
    private String awardName;
    /**
     * 奖品剩余库存
     */
    private Integer awardSurplusCount;

    // 中奖概率
    private BigDecimal awardRate;

    // 创建时间
    private String createTime;

    // 修改时间
    private String updateTime;
}
