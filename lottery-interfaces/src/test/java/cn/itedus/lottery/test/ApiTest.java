package cn.itedus.lottery.test;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.support.ids.IdGenerator;
import cn.itedus.lottery.infrastructure.dao.IActivityDao;
import cn.itedus.lottery.infrastructure.dao.IUserStrategyExportDao;
import cn.itedus.lottery.infrastructure.po.Activity;
import cn.itedus.lottery.infrastructure.po.UserStrategyExport;
import cn.itedus.lottery.rpc.IActivityBooth;
import cn.itedus.lottery.rpc.req.ActivityReq;
import cn.itedus.lottery.rpc.res.ActivityRes;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.*;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack)
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiTest {
    @Resource
    private IActivityDao activityDao;
    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Resource
    private Map<Constants.Ids, IdGenerator> idGeneratorMap;

    @Resource
    private IUserStrategyExportDao userStrategyExportDao;

    @Reference(interfaceClass = IActivityBooth.class, url = "dubbo://127.0.0.1:20880", timeout = 60000)
    private IActivityBooth activityBooth;
    @Test
    public void test_insert() {
        Activity activity = new Activity();
        activity.setActivityId(100003L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("仅用于插入数据测试");
        activity.setBeginDateTime(new Date());
        activity.setEndDateTime(new Date());
        activity.setStockCount(100);
        activity.setTakeCount(10);
        activity.setState(0);
        activity.setCreator("xiaofuge");
        activityDao.insert(activity);
    }

    @Test
    public void test_rpc(){
        ActivityReq req = new ActivityReq();
        req.setActivityId(100002L);
        ActivityRes result = activityBooth.queryActivityById(req);
        logger.info("测试结果:{}", JSON.toJSONString(result));
    }
    @Test
    public void test_insert1() {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        userStrategyExport.setuId("Uhdgkw766120d");
        userStrategyExport.setActivityId(idGeneratorMap.get(Constants.Ids.ShortCode).getId());
        userStrategyExport.setOrderId(idGeneratorMap.get(Constants.Ids.SnowFlake).getId());
        userStrategyExport.setStrategyId(idGeneratorMap.get(Constants.Ids.RandomNumeric).getId());
        userStrategyExport.setStrategyMode(Constants.StrategyMode.SINGLE.getCode());
        userStrategyExport.setGrantType(1);
        userStrategyExport.setGrantDate(new Date());
        userStrategyExport.setGrantState(1);
        userStrategyExport.setAwardId("1");
        userStrategyExport.setAwardType(Constants.AwardType.DESC.getCode());
        userStrategyExport.setAwardName("IMac");
        userStrategyExport.setAwardContent("奖品描述");
        userStrategyExport.setUuid(String.valueOf(userStrategyExport.getOrderId()));

        userStrategyExportDao.insert(userStrategyExport);
    }

    @Test
    public void test_select() {
        UserStrategyExport userStrategyExport = userStrategyExportDao.queryUserStrategyExportByUId("Uhdgkw766120d");
        logger.info("测试结果：{}", JSON.toJSONString(userStrategyExport));
    }

    @Test
    public void test_strategy() {
        SecureRandom random = new SecureRandom();
        int rate = random.nextInt(100);

        System.out.println("概率：" + rate);

        List<Map<String, String>> strategyList = new ArrayList<>();

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "一等奖：彩电");
            put("awardId", "10001");
            put("awardCount", "3");
            put("awardRate", "0.2");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "二等奖：冰箱");
            put("awardId", "10002");
            put("awardCount", "5");
            put("awardRate", "0.3");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "三等奖：洗衣机");
            put("awardId", "10003");
            put("awardCount", "10");
            put("awardRate", "0.5");
        }});

    }

    @Test
    public void test_ids() {
        logger.info("雪花算法策略，生成ID：{}", idGeneratorMap.get(Constants.Ids.SnowFlake).getId());
        logger.info("日期算法策略，生成ID：{}", idGeneratorMap.get(Constants.Ids.ShortCode).getId());
        logger.info("随机算法策略，生成ID：{}", idGeneratorMap.get(Constants.Ids.RandomNumeric).getId());
    }

    @Test
    public void test_idx() {

        Map<Integer, Integer> map = new HashMap<>();

        int HASH_INCREMENT = 0x61c88647;
        int hashCode = 0;
        for (int i = 1; i <= 100; i++) {
            hashCode = i * HASH_INCREMENT + HASH_INCREMENT;
            int idx = hashCode & (128 - 1);

            map.merge(idx, 1, Integer::sum);

            System.out.println("斐波那契散列：" + idx + " 普通散列：" + (String.valueOf(i).hashCode() & (128 - 1)));
        }

        System.out.println(map);
    }
}

class DrawStrategy {

    // 黄金分割点：(√5 - 1) / 2 = 0.6180339887     1.618:1 == 1:0.618，Math.pow(2, 32) * 0.6180339887 = 0x61c88647
    private final int HASH_INCREMENT = 0x61c88647;

    // 用于保存0-100，斐波那契散列索引的结果
    private String[] rateTuple = new String[128];

    /**
     *
     * 初始化概率索引数组
     * 不同奖项概率 0.20、0.30、0.50，调整为三个奖项的范围值；
     * 一等奖：0-20
     * 二等奖：21-30
     * 三等奖：51-100
     *
     * 再把各个区间范围填充到数组中，索引位置和对应的奖品值
     * @param drawConfig
     */
    public void initRateTuple(List<Map<String, String>> drawConfig) {
        int cursorVal = 0;
        for (Map<String, String> drawMap : drawConfig) {
            int rateVal = Integer.parseInt(drawMap.get("awardRate"));

            for (int i = cursorVal + 1; i <= (rateVal + cursorVal); i++) {
                // 计算数组索引并填充数据
                int hashCode = i * HASH_INCREMENT + HASH_INCREMENT;
                int idx = hashCode & (rateTuple.length - 1);
                rateTuple[idx] = drawMap.get("awardDesc");
            }

            cursorVal += rateVal;

        }
    }

    /**
     * 随机抽奖
     * @return 中奖结果
     */
    public String randomDraw() {
        // 随机数
        int rate = new SecureRandom().nextInt(100) + 1;
        // 索引位置
        int hashCode = rate * HASH_INCREMENT + HASH_INCREMENT;
        int idx = hashCode & (rateTuple.length - 1);
        return rateTuple[idx];
    }

}
