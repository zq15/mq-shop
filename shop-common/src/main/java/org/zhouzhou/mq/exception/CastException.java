package org.zhouzhou.mq.exception;

import lombok.extern.slf4j.Slf4j;
import org.zhouzhou.mq.constant.ShopCode;

@Slf4j
public class CastException {
    public static void cast(ShopCode shopCode) throws CustomerException {
        log.error(shopCode.toString());
        throw new CustomerException(shopCode);
    }
}
