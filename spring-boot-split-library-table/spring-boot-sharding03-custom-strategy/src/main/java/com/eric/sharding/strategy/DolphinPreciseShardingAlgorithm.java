package com.eric.sharding.strategy;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-23 15:37
 */
@Slf4j
public class DolphinPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        log.info("collection:" + JSON.toJSONString(availableTargetNames) + ",preciseShardingValue:" + JSON.toJSONString(shardingValue));

        //availableTargetNames: user_0,user_1
        //shardingValue: {"logicTableName":"user","columnName":"id","value":396416249350848512}
        //collection:["user_0","user_1"],preciseShardingValue:{"logicTableName":"user","columnName":"id","value":396416249350848512}
        //name为两张订单表 user_0 和 user_1
        for (String name : availableTargetNames) {
            //订单号取模 与 订单表 user_0 和 user_1 的尾号做比对，如相等，就直接返回 user_0 或 user_1
            if (name.endsWith(String.valueOf(shardingValue.getValue() % 2))) {
                log.info("return name: " + name);
                return name;
            }
        }
        return null;
    }
}
