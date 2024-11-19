package org.zhouzhou.mq;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.pojo.ShopOrder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
public class OrderServiceTest {
    @Resource
    private IOrderService orderService;

    @Test
    public void confirmOrderA() {
        Result result = orderService.confirmOrder(A());
        System.out.println(result);
    }

    @Test
    public void confirmOrderB() {
        Result result = orderService.confirmOrder(B());
        System.out.println(result);
    }

    @Test
    public void confirmOrderC() {
        Result result = orderService.confirmOrder(C());
        System.out.println(result);
    }

    // 同时包含优惠卷和余额扣减
    @Test
    public void confirmOrderD() {
        Result result = orderService.confirmOrder(D());
        System.out.println(result);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void confirmOrderE() {
        Result result = orderService.confirmOrder(E());
        System.out.println(result);
    }

    @Test
    public void confirmOrder() throws Exception {
        ExecutorService service = Executors.newCachedThreadPool();
        final CountDownLatch cdAnswer = new CountDownLatch(5);
        Thread t1 = new Thread() {
            @Override
            public void run() {
                cdAnswer.countDown();
                Result result = orderService.confirmOrder(A());
                System.out.println(result);
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                cdAnswer.countDown();
                Result result = orderService.confirmOrder(B());
                System.out.println(result);
            }
        };
        Thread t3 = new Thread() {
            @Override
            public void run() {
                cdAnswer.countDown();
                Result result = orderService.confirmOrder(C());
                System.out.println(result);
            }
        };
        Thread t4 = new Thread() {
            @Override
            public void run() {
                cdAnswer.countDown();
                Result result = orderService.confirmOrder(D());
                System.out.println(result);
            }
        };
        Thread t5 = new Thread() {
            @Override
            public void run() {
                cdAnswer.countDown();
                Result result = orderService.confirmOrder(E());
                System.out.println(result);
            }
        };
        service.execute(t1);
        service.execute(t2);
        service.execute(t3);
        service.execute(t4);
        service.execute(t5);
        cdAnswer.await();

        System.in.read();


    }

    public static ShopOrder A() {
        Long goodsId = 378715381063495688L;
        Long userId = 378715381059301373L;
        ShopOrder order = new ShopOrder();
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        order.setCouponId(null);
        order.setAddress("北京");
        order.setGoodsNumber(1);
        order.setGoodsPrice(new BigDecimal(1000));
        order.setGoodsAmount(new BigDecimal(1000));
        order.setShippingFee(BigDecimal.ZERO);
        order.setOrderAmount(new BigDecimal(1000));
        order.setMoneyPaid(new BigDecimal(0));
        return order;
    }

    public static ShopOrder B() {
        Long goodsId = 378715381063495688L;
        Long userId = 378715381059301374L;
        ShopOrder order = new ShopOrder();
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        order.setCouponId(null);
        order.setAddress("北京");
        order.setGoodsNumber(1);
        order.setGoodsPrice(new BigDecimal(1000));
        order.setGoodsAmount(new BigDecimal(1000));
        order.setShippingFee(BigDecimal.ZERO);
        order.setOrderAmount(new BigDecimal(1000));
        order.setMoneyPaid(new BigDecimal(100));
        return order;
    }

    public static ShopOrder C() {
        Long couponId = 345988230098857981L;
        Long goodsId = 378715381063495688L;
        Long userId = 378715381059301375L;
        ShopOrder order = new ShopOrder();
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        order.setCouponId(couponId);
        order.setAddress("北京");
        order.setGoodsNumber(1);
        order.setGoodsPrice(new BigDecimal(1000));
        order.setGoodsAmount(new BigDecimal(1000));
        order.setShippingFee(BigDecimal.ZERO);
        order.setOrderAmount(new BigDecimal(1000));
        order.setMoneyPaid(new BigDecimal(0));
        return order;
    }

    public static ShopOrder D() {
        Long couponId = 345988230098857984L;
        Long goodsId = 378715381063495688L;
        Long userId = 378715381059301376L;
        ShopOrder order = new ShopOrder();
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        order.setCouponId(couponId);
        order.setAddress("北京");
        order.setGoodsNumber(1);
        order.setGoodsPrice(new BigDecimal(1000));
        order.setGoodsAmount(new BigDecimal(1000));
        order.setShippingFee(BigDecimal.ZERO);
        order.setOrderAmount(new BigDecimal(1000));
        order.setMoneyPaid(new BigDecimal(100));
        return order;
    }

    public static ShopOrder E() {
        Long couponId = 345988230098857982L;
        Long goodsId = 378715381063495688L;
        Long userId = 378715381059301377L;
        ShopOrder order = new ShopOrder();
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        order.setCouponId(couponId);
        order.setAddress("北京");
        order.setGoodsNumber(1);
        order.setGoodsPrice(new BigDecimal(1000));
        order.setGoodsAmount(new BigDecimal(1000));
        order.setShippingFee(BigDecimal.ZERO);
        order.setOrderAmount(new BigDecimal(1000));
        order.setMoneyPaid(new BigDecimal(100));
        return order;
    }

}
