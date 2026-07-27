package com.edu.seiryo.admin.controller;


import com.edu.seiryo.admin.pojo.GoodsUnit;
import com.edu.seiryo.admin.service.GoodsUnitService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TianTian
 * @date 2022/1/19 8:54
 */
@Controller
@RequestMapping("/goodsUnit")
public class GoodsUnitController {
	@Resource
    private GoodsUnitService goodsUnitService;

	 /**
     * 	查询所有商品单位
     * 	前端添加/编辑商品goods_add_update.ftl时下拉选择单位
     * 	请求路径：/goodsUnit/list
     * 	返回格式：JSON 数组
     */
    @RequestMapping("list")
    @ResponseBody
    public List<GoodsUnit> list() {
        return goodsUnitService.findAll();
    }
    
    /**
     * 	【商品管理】模块用
     * 	add_update.ftl页面展示单位下拉框
     * 	add.update.js请求/goodsUnit/allGoodsUnits,查询所有商品单位（别名，给商品管理使用）
     */
    @RequestMapping("allGoodsUnits")
    @ResponseBody
    public List<GoodsUnit> allGoodsUnits() {
        return goodsUnitService.findAll();
    }

}
