package com.cisco.as.iot.opentsdb.utils;

/**
 * <p>Created with IDEA
 * <p>Author: laudukang
 * <p>Date: 2019/8/7
 * <p>Time: 16:54
 * <p>Version: 1.0
 * See http://opentsdb.net/docs/build/html/user_guide/query/filters.html#built-in-2-x-filters
 */
public enum FilterTypeEnum {

    literal_or,         //   精确匹配多项迭代值，多项迭代值以'|'分隔，大小写敏感
    ilteral_or,         //   精确匹配多项迭代值，多项迭代值以'|'分隔，忽略大小写
    not_literal_or,     //   通配符取非匹配，大小写敏感
    not_iliteral_or,    //  通配符取非匹配，忽略大小写
    wildcard,           //  通配符匹配，大小写敏感
    iwildcard,          //  通配符匹配，忽略大小写
    regexp              //  正则表达式匹配
}
