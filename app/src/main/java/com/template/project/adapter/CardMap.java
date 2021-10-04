package com.template.project.adapter;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 5Mall<zhangwei> on 2018/11/9
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface CardMap {
    Class value();
}
