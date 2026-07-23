package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.pojo.OverflowListGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.query.DamageListGoodsQuery;
import com.edu.seiryo.admin.query.OverflowListGoodsQuery;

import java.util.Map;

/**
 * 报溢单商品服务类
 * @author TianTian
 * @date 2022/1/21 13:42
 */
public interface OverflowListGoodsService extends IService<OverflowListGoods> {

    Map<String, Object> overflowListGoodsList(OverflowListGoodsQuery overflowListGoodsQuery);
}
