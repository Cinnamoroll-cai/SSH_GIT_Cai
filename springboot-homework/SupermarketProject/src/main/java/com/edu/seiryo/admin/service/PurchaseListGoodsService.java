package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.pojo.PurchaseListGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.query.PurchaseListGoodsQuery;

import java.util.Map;

/**
 * 进货单商品表服务类
 * @author TianTian
 * @date 2022/1/19 13:58
 */
public interface PurchaseListGoodsService extends IService<PurchaseListGoods> {
	/**
     * 	根据进货单ID查询明细（用于点击“货单”查看详情）
     * 	@param query 查询条件
     * 	@return 分页结果
     */
    Map<String, Object> purchaseListGoodsList(PurchaseListGoodsQuery query);
}
