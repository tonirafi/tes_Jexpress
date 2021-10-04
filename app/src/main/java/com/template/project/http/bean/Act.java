package com.template.project.http.bean;

/**
 * Created by 5Mall<zhangwei> on 2019-10-18
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class Act extends BaseBean {
    public boolean is_exipired = true; //表示活动是否过期 默认活动过期不展示
    public String act_id; //活动id
    public String act_title; //活动标题
    public String act_des; //活动描述
    public String act_cover; //活动封面图片地址
    public String act_link;  //活动链接地址
}
