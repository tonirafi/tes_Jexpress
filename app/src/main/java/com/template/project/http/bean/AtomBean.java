package com.template.project.http.bean;


import com.template.project.adapter.BaseCard;

/**
 * Created by 5Mall<zhangwei> on 2018/12/19
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class AtomBean extends BaseCard {
    public int id;
    public String name;

    public AtomBean(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
