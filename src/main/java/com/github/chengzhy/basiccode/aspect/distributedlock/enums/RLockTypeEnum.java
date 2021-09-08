package com.github.chengzhy.basiccode.aspect.distributedlock.enums;

/**
 * redisson锁类型
 * @author chengzhy
 * @date 2021/8/9 9:32
 **/
public enum RLockTypeEnum {
    /* 可重入锁 */
    REENTRANT_LOCK,
    /* 公平锁 */
    FAIR_LOCK
}
