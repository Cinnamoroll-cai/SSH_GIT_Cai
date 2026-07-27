package com.edu.seiryo.admin.controller;


import com.edu.seiryo.admin.model.RespBean;
import com.edu.seiryo.admin.pojo.Goods;
import com.edu.seiryo.admin.pojo.GoodsType;
import com.edu.seiryo.admin.query.GoodsQuery;
import com.edu.seiryo.admin.service.GoodsService;
import com.edu.seiryo.admin.service.GoodsTypeService;
import com.edu.seiryo.admin.service.GoodsUnitService;
import com.edu.seiryo.admin.utils.StringUtil;

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
	
	@Resource
    private GoodsTypeService goodsTypeService;

    @Resource
    private GoodsUnitService goodsUnitService;
	
	 /**
	  *	进货入库商品列表数据接口（分页查询）
      *	商品选择goods.gtl弹窗中请求 /goods/list
      *	请求参数：page, limit, typeId, goodsName
      */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(GoodsQuery goodsQuery) {
        return goodsService.goodslist(goodsQuery);
    }
    
    /**
     * 	商品管理页面列表展示数据接口（分页查询）
     * main.ftl主页点击超链接 <a href...dadata-tab="goods/index" >商品管理</a>
     * 	请求 /goods/index
     * 	请求参数：page, limit, typeId, goodsName
     * 	返回 goods.ftl 商品管理列表
     */
    @RequestMapping("index")
    public String index() {
    	return "goods/goods";
    }
    
    /**
     * 	进入添加、编辑商品页面
     * 	点击"添加商品" → goods.js：openAddOrUpdateGoodsDialog() → 弹窗 /goods/addOrUpdateGoodsPage
     * 	返回add_update.ftl视图
     */
    @RequestMapping("addOrUpdateGoodsPage")
    public String addOrUpdateGoodsPage(Integer id, Integer typeId, Model model) {
    	boolean isEdit = id != null && id > 0;
    	if (isEdit) {
            // 编辑：查询商品信息
            Goods goods = goodsService.getById(id);
            if (goods != null) {
                // 查询分类名称
                if (goods.getTypeId() != null) {
                    GoodsType type = goodsTypeService.getById(goods.getTypeId());
                    if (type != null) {
                        goods.setTypeName(type.getName());
                    }
                }
                // 查询单位名称
                if (!StringUtil.isEmpty(goods.getUnit())) {
                    // unit 存的是单位名称（字符串），直接赋值给 unitName
                    goods.setUnitName(goods.getUnit());
                }
                model.addAttribute("goods", goods);
            }
        } else if (typeId != null && typeId > 0) {
            // 新增时预设分类
            GoodsType type = goodsTypeService.getById(typeId);
            if (type != null) {
                model.addAttribute("goodsType", type);
            }
        }
    	model.addAttribute("isEdit", isEdit);  // 传递编辑标志
        return "goods/add_update";
    }
    
    /*
     *  add_update.gtl弹窗加载
     * 	加载单位下拉：/goodsUnit/allGoodsUnits
     * 	点击"选择"类别 → /goods/toGoodsTypePage → 弹出 goods_type.ftl
     *	 选择后调用 parent.getVal() 回传
     */
    
    /**
     * add_update.ftl点击“保存”新增商品
     * add.update.js提交到 /goods/save
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean save(Goods goods) {
    	goodsService.saveGoods(goods);
    	return RespBean.success("商品添加成功！");
    }
    
    /**
     * add_update.ftl点击“保存”更新商品
     * add.update.js提交到 /goods/update
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Goods goods) {
    	goodsService.updateGoods(goods);
    	return RespBean.success("商品更新成功！");
    }
    
    /**
     * goods.ftl点击“删除”删除商品（逻辑删除）
     * goods.js提交到 /goods/delete
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id) {
    	goodsService.deleteGoods(id);
    	return RespBean.success("商品删除成功！");
    }
    
    /**
     * add_update.ftl点击“选择”进入商品类别选择弹窗
     * add.update.js提交到/goods/toGoodsTypePage?typeId
     */
    @RequestMapping("toGoodsTypePage")
    public String toGoodsTypePage(Integer typeId, Model model) {
    	model.addAttribute("typeId", typeId);
    	return "goods/goods_type";
    }
}
