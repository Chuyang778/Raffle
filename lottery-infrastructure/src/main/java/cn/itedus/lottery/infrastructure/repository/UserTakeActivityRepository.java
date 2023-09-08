package cn.itedus.lottery.infrastructure.repository;

import cn.itedus.lottery.domain.activity.model.vo.DrawOrderVO;
import cn.itedus.lottery.domain.activity.repository.IUserTakeActivityRepository;
import cn.itedus.lottery.infrastructure.dao.IUserTakeActivityCountDao;
import cn.itedus.lottery.infrastructure.dao.IUserTakeActivityDao;
import cn.itedus.lottery.infrastructure.po.UserTakeActivity;
import cn.itedus.lottery.infrastructure.po.UserTakeActivityCount;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author ChuYang
 * @version 1.0
 */
@Component
public class UserTakeActivityRepository implements IUserTakeActivityRepository {
    @Resource
    private IUserTakeActivityDao userTakeActivityDao;

    @Resource
    private IUserTakeActivityCountDao userTakeActivityCountDao;

    @Override
    public int subtractionLeftCount(Long activityId, String activityName, Integer takeCount, Integer userTakeLeftCount, String uId, Date partakeDate) throws Exception {
        if (userTakeLeftCount == null) {
            return createUserTakeActivityCount(uId, activityId, takeCount);
        } else {
            UserTakeActivityCount userTakeActivityCount = new UserTakeActivityCount();
            userTakeActivityCount.setuId(uId);
            userTakeActivityCount.setActivityId(activityId);
            return userTakeActivityCountDao.updateLeftCount(userTakeActivityCount);
        }
    }

    @Override
    public void takeActivity(Long activityId, String activityName, Integer takeCount, Integer userTakeLeftCount, String uId, Date takeDate, Long takeId) {
        UserTakeActivity userTakeActivity = new UserTakeActivity();
        userTakeActivity.setuId(uId);
        userTakeActivity.setTakeId(takeId);
        userTakeActivity.setActivityId(activityId);
        userTakeActivity.setActivityName(activityName);
        userTakeActivity.setTakeDate(takeDate);
        if (null == userTakeLeftCount) {
            userTakeActivity.setTakeCount(1);
        } else {
            userTakeActivity.setTakeCount(takeCount - userTakeLeftCount);
        }
        String uuid = uId + "_" + activityId + "_" + userTakeActivity.getTakeCount();
        userTakeActivity.setUuid(uuid);

        userTakeActivityDao.insert(userTakeActivity);
    }

    @Override
    public int lockTackActivity(String uId, Long activityId, Long takeId) {
        UserTakeActivity userTakeActivity = new UserTakeActivity();
        userTakeActivity.setuId(uId);
        userTakeActivity.setActivityId(activityId);
        userTakeActivity.setTakeId(takeId);
        return userTakeActivityDao.lockTackActivity(userTakeActivity);
    }

    @Override
    public void saveUserStrategyExport(DrawOrderVO drawOrder) {

    }

    private int createUserTakeActivityCount(String uId, Long activityId, Integer takeCount) throws Exception {
        Class<?> UserTakeActivityCountclass = Class.forName("cn.itedus.lottery.infrastructure.po.UserTakeActivityCount");
        Method setuId = UserTakeActivityCountclass.getDeclaredMethod("setuId", String.class);
        Method setActivityId = UserTakeActivityCountclass.getDeclaredMethod("setActivityId", Long.class);
        Method setTotalCount = UserTakeActivityCountclass.getDeclaredMethod("setTotalCount", Integer.class);
        Method setLeftCount = UserTakeActivityCountclass.getDeclaredMethod("setLeftCount", Integer.class);
        UserTakeActivityCount instance = (UserTakeActivityCount) UserTakeActivityCountclass.newInstance();
        setuId.invoke(instance, uId);
        setActivityId.invoke(instance, activityId);
        setTotalCount.invoke(instance, takeCount);
        setLeftCount.invoke(instance, takeCount - 1);
        userTakeActivityCountDao.insert(instance);
        return 1;

    }
}
