package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.pojo.PurchaseList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.pojo.PurchaseListGoods;
import com.edu.seiryo.admin.query.PurchaseListQuery;

import java.util.List;
import java.util.Map;

/**
 * 进货单服务类
 * @author TianTian
 * @date 2022/1/19 13:58
 */
public interface PurchaseListService extends IService<PurchaseList> {
	/**
	 * 	生成新的进货单号
	 * 	@return 单号字符串，如 "JH202603210001"
     */
    String getNextPurchaseNumber();

    /**
     * 	保存进货单（主表和明细表）
     * 	@param purchaseList 主表对象
     * 	@param goodsList    明细列表（商品）
     */
    void savePurchaseList(PurchaseList purchaseList, List<PurchaseListGoods> goodsList);

    /**
     * 	分页查询进货单列表（用于进货单据查询页面）
     * 	@param query 查询条件对象
     * 	@return 分页结果 Map
     */
    Map<String, Object> purchaseList(PurchaseListQuery query);

    /**
     * 	根据ID删除进货单（主表和明细）
     * 	@param id 主表ID
     */
    void deletePurchaseList(Integer id);
}
