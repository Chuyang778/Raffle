package cn.itedus.lottery.domain.strategy.service.algorithm;

import cn.itedus.lottery.domain.strategy.model.vo.AwardRateInfo;

import java.util.List;

/**
 * @author ChuYang
 * @version 1.0
 */

/**
 * 策略模式 + 模板模式
 */
public interface IDrawAlgorithm {
    /**
     * 采用与HashMap类似的扰动哈希算法，要求数组的长度是2的幂
     * @param strategyId
     * @param awardRateInfoList
     */

    void initRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList);

    boolean isExistRateTuple(Long strategyId);

    String randomDraw(Long strategyId, List<String> excludeAwardIds);
}
