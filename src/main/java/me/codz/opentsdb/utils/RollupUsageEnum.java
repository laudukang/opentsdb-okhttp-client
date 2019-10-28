package me.codz.opentsdb.utils;

/**
 * <p>Created with IDEA
 * <p>Author: laudukang
 * <p>Date: 2019/10/28
 * <p>Time: 11:22
 * <p>Version: 1.0
 */
public enum RollupUsageEnum {
    ROLLUP_RAW,             //skip rollups
    ROLLUP_NOFALLBACK,      //only query the auto-detected rollup table
    ROLLUP_FALLBACK,        //fallback to matching rollup tables in sequence
    ROLLUP_FALLBACK_RAW     //fall back to the raw table if nothing was found in the first auto table
}
