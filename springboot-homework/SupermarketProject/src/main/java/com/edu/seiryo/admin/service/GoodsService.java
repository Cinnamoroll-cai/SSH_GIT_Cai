package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.query.GoodsQuery;

import java.io.Serializable;
import java.util.Map;

/**
 * 商品表服务
 * @author TianTian
 * @date 2022/1/19 13:55
 */
public interface GoodsService extends IService<Goods> {
	 /**
     * 	分页查询商品列表
     * 	前端商品选择弹窗goods.ftl中请求 /goods/list
     * @param query 查询条件（page, limit, typeId, goodsName）
     * @return layui 表格格式
     */
	Map<String, Object> goodslist(GoodsQuery query);
	
	/**
     * 	库存查询（分页）
     * 	前端当前库存查询页面stock_search.ftl请求 /common/stockList
     */
    Map<String, Object> stockList(GoodsQuery query);
    
    /**
     * 	保存商品（新增）
     */
    void saveGoods(Goods goods);

    /**
     * 	更新商品
     */
    void updateGoods(Goods goods);

    /**
     * 	逻辑删除商品
     */
    void deleteGoods(Integer id);
    
    /**
     * 	库存预警
     */
    Map<String, Object> alarmList(GoodsQuery query);
}
