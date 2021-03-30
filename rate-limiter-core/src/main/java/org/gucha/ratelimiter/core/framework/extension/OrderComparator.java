package org.gucha.ratelimiter.core.framework.extension;

import java.util.Comparator;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 上午10:29
 */
public class OrderComparator implements Comparator<Object> {
    public static final OrderComparator INSTANCE = new OrderComparator();

    private OrderComparator() {

    }

    @Override
    public int compare(Object o1, Object o2) {
        Order order1 = o1.getClass().getAnnotation(Order.class);
        Order order2 = o2.getClass().getAnnotation(Order.class);
        int value1 = order1 == null ? Order.LOWEST_PRECEDENCE : order1.value();
        int value2 = order2 == null ? Order.LOWEST_PRECEDENCE : order2.value();
        rangeCheck(value1);
        rangeCheck(value2);
        return value1 - value2;
    }

    private void rangeCheck(int value) {
        if (value > Order.LOWEST_PRECEDENCE || value < Order.HIGHEST_PRECEDENCE) {
            throw new IndexOutOfBoundsException(String.format("Order value: %d (expected: value range[%d, %d])",
                    value, Order.HIGHEST_PRECEDENCE, Order.LOWEST_PRECEDENCE));
        }
    }
}
