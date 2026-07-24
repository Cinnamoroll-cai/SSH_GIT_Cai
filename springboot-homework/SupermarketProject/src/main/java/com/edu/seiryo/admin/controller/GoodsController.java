package com.edu.seiryo.admin.controller;


import com.edu.seiryo.admin.model.RespBean;
import com.edu.seiryo.admin.pojo.Goods;
import com.edu.seiryo.admin.query.GoodsQuery;
import com.edu.seiryo.admin.service.GoodsService;
import com.edu.seiryo.admin.service.GoodsTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 商品控制器
 * @author TianTian
 * @date 2022/1/18 22:50
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Resource
    private GoodsService goodsService;
	
	 /**
     * 商品列表数据接口（分页查询）
     * 商品选择goods.gtl弹窗中请求 /goods/list
     * 请求参数：page, limit, typeId, goodsName
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(GoodsQuery goodsQuery) {
        return goodsService.goodslist(goodsQuery);
    }
}
