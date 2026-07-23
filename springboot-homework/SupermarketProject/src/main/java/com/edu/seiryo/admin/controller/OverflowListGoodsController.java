package com.edu.seiryo.admin.controller;


import com.edu.seiryo.admin.query.DamageListGoodsQuery;
import com.edu.seiryo.admin.query.OverflowListGoodsQuery;
import com.edu.seiryo.admin.service.OverflowListGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 报溢单商品表
 * @author TianTian
 * @date 2022/1/21 13:40
 */
@Controller
@RequestMapping("/overflowListGoods")
public class OverflowListGoodsController {

    @Resource
    private OverflowListGoodsService overflowListGoodsService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> overflowListGoodsList(OverflowListGoodsQuery overflowListGoodsQuery){
        return overflowListGoodsService.overflowListGoodsList(overflowListGoodsQuery);
    }

}
