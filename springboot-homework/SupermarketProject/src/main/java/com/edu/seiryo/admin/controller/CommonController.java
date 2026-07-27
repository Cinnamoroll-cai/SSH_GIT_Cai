package com.edu.seiryo.admin.controller;

import com.edu.seiryo.admin.model.GoodsModel;
import com.edu.seiryo.admin.pojo.Goods;
import com.edu.seiryo.admin.pojo.GoodsType;
import com.edu.seiryo.admin.pojo.GoodsUnit;
import com.edu.seiryo.admin.query.GoodsQuery;
import com.edu.seiryo.admin.service.GoodsService;
import com.edu.seiryo.admin.service.GoodsTypeService;
import com.edu.seiryo.admin.service.GoodsUnitService;
import com.edu.seiryo.admin.utils.StringUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author TianTian
 * @date 2022/1/19 14:06
 */
@Controller
@RequestMapping("common")
public class CommonController {
    @Resource
    private GoodsService goodsService;

    @Resource
    private GoodsTypeService goodsTypeService;  // 注入分类Service

    @Resource
    private GoodsUnitService goodsUnitService;  // 注入单位Service

    /**
     * 	添加商品-选择商品页
     * @return
     */
    @RequestMapping("toSelectGoodsPage")
    public String toSelectGoodsPage(){
        return "common/goods";
    }

    /**
     * 	添加商品-商品信息添加页(单价、进货数量)
     * @param gid
     * @param model
     * @return
     */
    @RequestMapping("toAddGoodsInfoPage")
    public String toGoodsInfoPage(Integer gid, Model model){
    	 // 1. 根据 gid 查询商品信息
        Goods goods = goodsService.getById(gid);
        if (goods != null) {
            // 2. 查询分类名称
            if (goods.getTypeId() != null) {
                GoodsType type = goodsTypeService.getById(goods.getTypeId());
                if (type != null) {
                    goods.setTypeName(type.getName());
                }
            }
            // 3. 查询单位名称
            if (!StringUtil.isEmpty(goods.getUnit())) {
                try {
                    Integer unitId = Integer.parseInt(goods.getUnit());
                    GoodsUnit unit = goodsUnitService.getById(unitId);
                    if (unit != null) {
                        goods.setUnitName(unit.getName());
                    }
                } catch (NumberFormatException e) {
                    goods.setUnitName(goods.getUnit());
                }
            }
            // 4. 放入 Model，同时设置 flag=0 表示新增
            model.addAttribute("goods", goods);
            model.addAttribute("flag", 0);
        }
        return "common/goods_add_update";
    }


    /**
     * 	修改商品-商品信息修改页(单价、进货数量)
     * @param goodsModel
     * @param model
     * @return
     */
    @RequestMapping("toUpdateGoodsInfoPage")
    public String toUpdateGoodsInfoPage(GoodsModel goodsModel, Model model){
    	// goodsModel 中包含前端传来的 gid, price, num
        Integer gid = goodsModel.getId();  // 注意前端传的是 id 还是 gid
        if (gid != null) {
            Goods goods = goodsService.getById(gid);
            if (goods != null) {
            	// 2. 查询分类名称
                if (goods.getTypeId() != null) {
                    GoodsType type = goodsTypeService.getById(goods.getTypeId());
                    if (type != null) {
                        goods.setTypeName(type.getName());
                    }
                }
                // 3. 查询单位名称
                if (!StringUtil.isEmpty(goods.getUnit())) {
                    // 这里假设 unit 存的是单位名称（如 '袋'），而不是 ID
                    goods.setUnitName(goods.getUnit());
                }
                // 将前端传过来的价格和数量设置到 goods 对象中（用于回显）
                goods.setPurchasingPrice(goodsModel.getPrice()); // 用成本价字段暂存单价
                // 注意：这里需要给 goods 添加一个临时字段 num，或者在 FTL 中单独取值
                model.addAttribute("num", goodsModel.getNum());
                model.addAttribute("price", goodsModel.getPrice());
                model.addAttribute("goods", goods);
                model.addAttribute("flag", 1); // 编辑模式
            }
        }
        return "common/goods_add_update";
    }


    /**
     * 当前库存页
     * @return
     */
    @RequestMapping("toGoodsStockPage")
    public String toGoodsStockPage() {
        return "common/stock_search";
    }


    /**
     * 库存列表数据接口
     * 前端：stock.search.js 请求 /common/stockList
     */
    @RequestMapping("stockList")
    @ResponseBody
    public Map<String,Object> stockLick(GoodsQuery goodsQuery){
        return goodsService.stockList(goodsQuery);
    }


    /**
     * 商品报损|报溢查询页
     * @return
     */
    @RequestMapping("toDamageOverflowSearchPage")
    public String toDamageOverflowSearchPage(){
        return "common/damage_overflow_search";
    }


    /**
     * 库存报警页
     * @return
     */
    @RequestMapping("alarmPage")
    public String alarmPage(){
        return "common/alarm";
    }


    /**
     * 库存报警查询接口
     * @param goodsQuery
     * @return
     */
    @RequestMapping("listAlarm")
    @ResponseBody
    public Map<String,Object> listAlarm(GoodsQuery goodsQuery){
        goodsQuery.setType(3);
        return null;
    }







}
