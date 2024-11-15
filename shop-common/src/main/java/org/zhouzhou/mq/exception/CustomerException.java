package org.zhouzhou.mq.exception;

import org.zhouzhou.mq.constant.ShopCode;

public class CustomerException extends RuntimeException {

    private ShopCode shopCode;

    public CustomerException(ShopCode shopCode) {
        this.shopCode = shopCode;

    }
}
