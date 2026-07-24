package com.edu.seiryo.admin.controller;


import com.edu.seiryo.admin.dto.TreeDto;
import com.edu.seiryo.admin.model.RespBean;
import com.edu.seiryo.admin.pojo.GoodsType;
import com.edu.seiryo.admin.service.GoodsTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author TianTian
 * @date 2022/1/19 8:36
 */
@Controller
@RequestMapping("/goodsType")
public class GoodsTypeController {
	@Resource
    private GoodsTypeService goodsTypeService;

	 /**
     * 查询所有商品分类（树形结构）
     * 前端商品选择弹窗goods.ftl左侧分类树
     * 请求路径：/goodsType/queryAllGoodsTypes
     * 返回格式：树形 JSON
     */
    @RequestMapping("queryAllGoodsTypes")
    @ResponseBody
    public List<Map<String, Object>> queryAllGoodsTypes(){
    	return goodsTypeService.queryAllGoodsType();
    }

}
