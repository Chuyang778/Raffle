package cn.itedus.lottery.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.itedus.lottery.infrastructure.po.UserTakeActivity;
import cn.itedus.lottery.infrastructure.po.UserTakeActivityCount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChuYang
 * @version 1.0
 */
@Mapper
public interface IUserTakeActivityCountDao {

    /**
     * 查询用户领取次数信息
     * @param userTakeActivityCountReq
     * @return
     */
    @DBRouter
    UserTakeActivityCount queryUserTakeActivityCount(UserTakeActivityCount userTakeActivityCountReq);


    /**
     * 插入领取次数信息
     * @param userTakeActivityCount
     */
    void insert(UserTakeActivityCount userTakeActivityCount);


    /**
     * 更新领取次数信息
     * @param userTakeActivityCount
     * @return
     */
    int updateLeftCount(UserTakeActivityCount userTakeActivityCount);
}
