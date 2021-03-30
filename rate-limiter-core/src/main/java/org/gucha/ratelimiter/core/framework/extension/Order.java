package org.gucha.ratelimiter.core.framework.extension;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 上午10:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Order {

    int HIGHEST_PRECEDENCE = 0;
    int LOWEST_PRECEDENCE = 100;

    int value() default LOWEST_PRECEDENCE;

}
