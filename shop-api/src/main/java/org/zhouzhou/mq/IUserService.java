package org.zhouzhou.mq;

import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.pojo.ShopUser;
import org.zhouzhou.mq.pojo.ShopUserMoneyLog;

public interface IUserService {
    /**
     * 根据ID查询用户
     *
     * @param userId
     * @return
     */
    ShopUser findOne(Long userId);

    /**
     * 更新用户余额
     *
     * @param userMoneyLog
     * @return
     */
    Result updateMoneyPaid(ShopUserMoneyLog userMoneyLog);
}
