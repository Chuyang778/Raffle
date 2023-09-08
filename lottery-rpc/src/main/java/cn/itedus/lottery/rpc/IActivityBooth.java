package cn.itedus.lottery.rpc;

import cn.itedus.lottery.rpc.req.ActivityReq;
import cn.itedus.lottery.rpc.res.ActivityRes;

/**
 * @author ChuYang
 * @version 1.0
 */
public interface IActivityBooth {
    ActivityRes queryActivityById(ActivityReq req);
}
