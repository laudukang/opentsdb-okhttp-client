package com.cisco.as.iot.opentsdb.utils;

/**
 * <p>Created with IDEA
 * <p>Author: laudukang
 * <p>Author: shifeng 2016/5/19
 * <p>Date: 2019/8/6
 * <p>Time: 20:46
 * <p>Version: 1.0
 * <p>
 * See https://www.docs4dev.com/docs/zh/opentsdb/2.3/reference/user_guide-query-aggregators.html
 * See http://opentsdb.net/docs/build/html/user_guide/query/aggregators.html#available-aggregators
 */
public enum AggregatorEnum {

    avg,//	since 1.0	平均数据点	Linear Interpolation
    count,//	2.2	集合中的原始数据点数	如果缺少则为零
    dev,//	1.0	计算标准偏差	Linear Interpolation
    ep50r3,//	2.2	使用R-3方法计算估计的第50个百分点*	线性插值
    ep50r7,//	2.2	使用R-7方法计算估计的第50个百分点*	线性插值
    ep75r3,//	2.2	使用R-3方法计算估计的第75百分位*	线性插值
    ep75r7,//	2.2	使用R-7方法计算估计的第75百分位*	线性插值
    ep90r3,//	2.2	使用R-3方法计算估计的第90个百分点*	线性插值
    ep90r7,//	2.2	使用R-7方法计算估计的第90个百分点*	线性插值
    ep95r3,//	2.2	使用R-3方法计算估计的第95百分位*	线性插值
    ep95r7,//	2.2	使用R-7方法计算估计的第95百分位*	线性插值
    ep99r3,//	2.2	使用R-3方法计算估计的第99百分位*	线性插值
    ep99r7,//	2.2	使用R-7方法计算估计的第99百分位*	线性插值
    ep999r3,//	2.2	计算估计的999th使用R-3方法进行百分位数*	线性插值
    ep999r7,//	2.2	使用R-7方法计算估计的第999个百分点*	线性插值
    first,//	2.3	返回集合中的第一个数据点。仅对下采样有用，而不是聚合。	不确定
    last,//	2.3	返回集合中的最后一个数据点。仅对下采样有用，而不是聚合。	不确定
    mimmin,//	2.0	选择最小数据点	最大值如果缺少
    mimmax,//	2.0	选择最大数据点	如果缺少最小
    min,//	1.0	选择最小的数据点	Linear Interpolation
    max,//	1.0	选择最大数据点	Linear Interpolation
    none,//	2.3	按所有时间序列的聚合跳过组。	零如果失踪
    p50,//	2.2	计算第50个百分点	线性插值
    p75,//	2.2	计算第75个百分点	线性插值
    p90,//	2.2	计算第90个百分点	线性插值
    p95,//	2.2	计算第95百分位	线性插值
    p99,//	2.2	计算第99个百分点	线性插值
    p999,//	2.2	计算第999个百分点	线性插值
    sum,//	1.0	将数据点一起添加	Linear Interpolation
    zimsum//	2.0	将数据点一起添加	如果缺少则为零
}
