package com.edu.seiryo.admin.controller;


import com.edu.seiryo.admin.query.DamageListGoodsQuery;
import com.edu.seiryo.admin.query.PurchaseListGoodsQuery;
import com.edu.seiryo.admin.service.DamageListGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 报损单商品表控制器
 * @author TianTian
 * @date 2022/1/21 13:38
 */
@Controller
@RequestMapping("/damageListGoods")
public class DamageListGoodsController {


    @Resource
    private DamageListGoodsService damageListGoodsService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> damageListGoodsList(DamageListGoodsQuery damageListGoodsQuery){
        return damageListGoodsService.damageListGoodsList(damageListGoodsQuery);
    }



}
