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

import java.util.HashMap;
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
     * 	查询所有商品分类（树形结构）
     * 	前端商品选择弹窗goods.ftl左侧分类树
     * 	请求路径：/goodsType/queryAllGoodsTypes
     * 	返回格式：树形 JSON
     */
    @RequestMapping("queryAllGoodsTypes")
    @ResponseBody
    public List<Map<String, Object>> queryAllGoodsTypes(){
    	return goodsTypeService.queryAllGoodsType();
    }
    
    /**
     * 	首页点击“商品分类管理”
     * 	请求/goodsType/index
     *	返回"goodsType/goods_type"进入商品分类管理主页
     */
    @RequestMapping("index")
    public String index() {
    	return "goodsType/goods_type";
    }
    
    /**
     * 	前端goods_type.ftl查询所有分类，渲染树形表格数据
     * 	goods.type.js请求/goodsType/list
     * 	返回扁平列表，treetable 根据 pId 自动构建树
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(){
    	List<GoodsType> list = goodsTypeService.list();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("data", list);
        return result;
    }
    
    /**
     * 	前端goods_type.ftl点击“子类别”，进入子类别添加画面
     *  goods.type.js请求/goodsType/addGoodsTypePage?pId
     * 	 返回"goodsType/add"进入子类别添加页面
     */
    @RequestMapping("addGoodsTypePage")
    public String addGoodsTypePage(Integer pId, Model model) {
    	model.addAttribute("pId", pId);
    	return "goodsType/add";
    }
    
    /**
     * 	保存新类别
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean save(GoodsType goodsType) {
        goodsTypeService.saveGoodsType(goodsType);
        return RespBean.success("添加成功");
    }

    /**
     * 	删除类别（检查是否有子节点）
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id) {
        goodsTypeService.deleteGoodsType(id);
        return RespBean.success("删除成功");
    }
}
