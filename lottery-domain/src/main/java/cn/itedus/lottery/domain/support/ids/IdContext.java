package cn.itedus.lottery.domain.support.ids;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.support.ids.policy.RandomNumeric;
import cn.itedus.lottery.domain.support.ids.policy.ShortCode;
import cn.itedus.lottery.domain.support.ids.policy.SnowFlake;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChuYang
 * @version 1.0
 */
@Configuration
public class IdContext {

    @Bean
    public Map<Constants.Ids, IdGenerator> idGenerator(SnowFlake snowFlake, ShortCode shortCode, RandomNumeric randomNumeric) {
        ConcurrentHashMap<Constants.Ids, IdGenerator> idGeneratorMap = new ConcurrentHashMap<>();
        idGeneratorMap.putIfAbsent(Constants.Ids.SnowFlake, snowFlake);
        idGeneratorMap.putIfAbsent(Constants.Ids.ShortCode, shortCode);
        idGeneratorMap.putIfAbsent(Constants.Ids.RandomNumeric, randomNumeric);
        return idGeneratorMap;
    }
}
