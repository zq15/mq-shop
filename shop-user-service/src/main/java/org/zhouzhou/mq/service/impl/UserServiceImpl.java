package org.zhouzhou.mq.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zhouzhou.mq.IUserService;
import org.zhouzhou.mq.constant.ShopCode;
import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.exception.CastException;
import org.zhouzhou.mq.mapper.ShopUserMapper;
import org.zhouzhou.mq.mapper.ShopUserMoneyLogMapper;
import org.zhouzhou.mq.pojo.ShopUser;
import org.zhouzhou.mq.pojo.ShopUserMoneyLog;
import org.zhouzhou.mq.pojo.ShopUserMoneyLogExample;

@Slf4j
@Component
@Service(interfaceClass = IUserService.class)
public class UserServiceImpl implements IUserService {

    @Resource
    private ShopUserMapper userMapper;

    @Resource
    private ShopUserMoneyLogMapper userMoneyLogMapper;

    @Override
    public ShopUser findOne(Long userId) {
        if (userId == null) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public Result updateMoneyPaid(ShopUserMoneyLog userMoneyLog) {
        // 1 判断请求参数是否合法
        if (userMoneyLog == null
            || userMoneyLog.getUserId() == null
            || userMoneyLog.getUseMoney() == null
            || userMoneyLog.getOrderId() == null
            || userMoneyLog.getUseMoney().compareTo(BigDecimal.ZERO) <= 0) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }

        // 2 查询该订单是否存在付款记录
        ShopUserMoneyLogExample userMoneyLogExample = new ShopUserMoneyLogExample();
        userMoneyLogExample.createCriteria()
            .andUserIdEqualTo(userMoneyLog.getUserId())
            .andOrderIdEqualTo(userMoneyLog.getOrderId());

        long r = userMoneyLogMapper.countByExample(userMoneyLogExample);

        ShopUser user = userMapper.selectByPrimaryKey(userMoneyLog.getUserId());

        // 3 扣减金额
        if (userMoneyLog.getMoneyLogType().intValue() == ShopCode.SHOP_USER_MONEY_PAID.getCode().intValue()) {
            if (r > 0) {
                return new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode(),
                    ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getMessage());
            }
            // 扣减金额
            user.setUserMoney(user.getUserMoney().subtract(userMoneyLog.getUseMoney()));
            userMapper.updateByPrimaryKey(user);
        }

        // 4. 回退金额
        if (userMoneyLog.getMoneyLogType().intValue() == ShopCode.SHOP_USER_MONEY_REFUND.getCode().intValue()) {
            if (r < 0) {
                return new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY.getCode(),
                    ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY.getMessage());
            }
            // 防止多次退款 -> 查询之前有没有这一条退款的日志
            ShopUserMoneyLogExample userMoneyLogExample1 = new ShopUserMoneyLogExample();
            userMoneyLogExample1.createCriteria()
                .andUserIdEqualTo(userMoneyLog.getUserId())
                .andOrderIdEqualTo(userMoneyLog.getOrderId())
                .andMoneyLogTypeEqualTo(ShopCode.SHOP_USER_MONEY_REFUND.getCode());

            long r2 = userMoneyLogMapper.countByExample(userMoneyLogExample1);
            if (r2 > 0) {//已退过款
                return new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_USER_MONEY_REDUCE_ALREADY.getCode(), ShopCode.SHOP_USER_MONEY_REDUCE_ALREADY.getMessage());
            }
            // 退款加余额
            user.setUserMoney(user.getUserMoney().add(userMoneyLog.getUseMoney()));
            userMapper.updateByPrimaryKey(user);
        }

        //5记录订单余额使用日志
        userMoneyLog.setCreateTime(new Date());
        Result result;
        try {
            userMoneyLogMapper.insert(userMoneyLog);
            result = new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
        } catch (Exception e) {
            log.info(e.toString());
            result = new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_FAIL.getMessage());
        }
        return result;
    }
}
