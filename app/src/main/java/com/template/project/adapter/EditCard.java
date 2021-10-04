package com.template.project.adapter;

import java.util.Map;

/**
 * Created by 5Mall<zhangwei> on 2019/1/8
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public abstract class EditCard extends BaseCard {

    public String _key = this.getClass().getSimpleName();
    public boolean skipCollect = false;

    public Map<String, Object> collect(Map<String, Object> map) {
        if (!skipCollect) {
            map.put(_key, getValue());
        }
        return map;
    }

    public abstract Object getValue();
}
