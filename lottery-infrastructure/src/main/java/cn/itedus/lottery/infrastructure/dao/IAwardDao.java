package cn.itedus.lottery.infrastructure.dao;

import cn.itedus.lottery.infrastructure.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ChuYang
 * @version 1.0
 */
@Mapper
public interface IAwardDao {
    Award queryAwardInfo(String awardId);

    void insertList(List<Award> list);
}
