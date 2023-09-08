package cn.itedus.lottery.domain.strategy.service.algorithm;

import cn.itedus.lottery.domain.strategy.model.vo.AwardRateInfo;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChuYang
 * @version 1.0
 */
public abstract class BaseAlgorithm implements IDrawAlgorithm {

    // 斐波那契增量
    private final int HASH_INCREMENT = 0x61c88647;

    private final int RATE_TUPLE_LENGTH = 128;

    protected Map<Long, String[]> rateTupleMap = new ConcurrentHashMap<>();
    protected Map<Long, List<AwardRateInfo>> awardRateInfoMap = new ConcurrentHashMap<>();

    @Override
    public synchronized void initRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList) {
        //前置判断
        if (isExistRateTuple(strategyId)) return;
        awardRateInfoMap.put(strategyId, awardRateInfoList);
        String[] rateTuple = rateTupleMap.computeIfAbsent(strategyId, k -> new String[RATE_TUPLE_LENGTH]);
        int cursorVal = 0;
        for (AwardRateInfo awardRateInfo : awardRateInfoList) {
            int rateVal = awardRateInfo.getAwardRate().multiply(new BigDecimal(100)).intValue();
            for (int i = cursorVal + 1; i <= cursorVal + rateVal; i++) {
                rateTuple[hashIdx(i)] = awardRateInfo.getAwardId();
            }
            cursorVal += rateVal;
        }
    }

    @Override
    public boolean isExistRateTuple(Long strategyId) {
        return awardRateInfoMap.containsKey(strategyId);
    }


    protected int hashIdx(int val) {
        int hash = val * HASH_INCREMENT + HASH_INCREMENT;
        return hash & (RATE_TUPLE_LENGTH - 1);
    }

    protected int generateSecureRandomIntCode(int bound) {
        return new SecureRandom().nextInt(bound) + 1;
    }
}
