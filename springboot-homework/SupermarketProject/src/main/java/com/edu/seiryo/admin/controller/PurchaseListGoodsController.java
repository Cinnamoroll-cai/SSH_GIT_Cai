package com.edu.seiryo.admin.controller;


import com.edu.seiryo.admin.query.PurchaseListGoodsQuery;
import com.edu.seiryo.admin.query.PurchaseListQuery;
import com.edu.seiryo.admin.service.PurchaseListGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 进货单商品表
 * @author TianTian
 * @date 2022/1/19 12:32
 */
@Controller
@RequestMapping("/purchaseListGoods")
public class PurchaseListGoodsController {
	@Resource
    private PurchaseListGoodsService purchaseListGoodsService;
	
	/**
     * 	查询某个进货单的商品明细
     * 	点击“货单”按钮，会打开一个弹窗，请求此接口
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> purchaseListGoodsList(PurchaseListGoodsQuery purchaseListGoodsQuery) {
        return purchaseListGoodsService.purchaseListGoodsList(purchaseListGoodsQuery);
    }
}
