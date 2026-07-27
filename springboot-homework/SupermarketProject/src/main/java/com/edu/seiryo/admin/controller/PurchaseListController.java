package com.edu.seiryo.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.edu.seiryo.admin.model.RespBean;
import com.edu.seiryo.admin.pojo.PurchaseList;
import com.edu.seiryo.admin.pojo.PurchaseListGoods;
import com.edu.seiryo.admin.query.PurchaseListGoodsQuery;
import com.edu.seiryo.admin.query.PurchaseListQuery;
import com.edu.seiryo.admin.service.PurchaseListGoodsService;
import com.edu.seiryo.admin.service.PurchaseListService;

import com.edu.seiryo.admin.service.UserService;
import com.edu.seiryo.admin.utils.AssertUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 进货单控制器
 * @author TianTian
 * @date 2022/1/19 12:32
 */
@Controller
@RequestMapping("/purchase")
public class PurchaseListController {
	@Resource
    private PurchaseListService purchaseListService;

    @Resource
    private UserService userService;
    
    @Resource
    private PurchaseListGoodsService purchaseListGoodsService;  // 新增注入
    
    /**
     * 	进入进货入库页面
     * 	前端菜单：data-tab="purchase/index" → 请求 /purchase/index
     * 	返回视图：/views/purchase/purchase.ftl
     * 	并携带单号（purchaseNumber）
     */
    @RequestMapping("index")
    public String index(Model model) {
        // 生成进货单号
        String purchaseNumber = purchaseListService.getNextPurchaseNumber();
        model.addAttribute("purchaseNumber", purchaseNumber);
     // 返回视图名，Spring Boot 会查找 classpath:/views/purchase/purchase.ftl
        return "purchase/purchase"; 
    }
    
    /**
     * 	保存进货单（包含主表和明细）
     * 	前端：点击“保存”按钮，会提交表单（POST /purchase/save）
     * 	参数 goodsJson 是前端表格中商品数据的 JSON 字符串
     * 	Principal 获取当前登录用户名
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean save(PurchaseList purchaseList, String goodsJson, Principal principal) {
    	// 获取当前操作用户ID
    	String userName = principal.getName();
        purchaseList.setUserId(userService.findForName(userName).getId());
        
        // 解析商品JSON为List<PurchaseListGoods>
        Gson gson = new Gson();
        List<PurchaseListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<PurchaseListGoods>>(){}.getType());
        
        // 调用Service保存
        purchaseListService.savePurchaseList(purchaseList, plgList);
        return RespBean.success("商品进货入库成功!");
    }
    
    /**
     * 	进入进货单据查询页面
     * 	前端菜单：data-tab="purchase/searchPage" → 请求 /purchase/searchPage
     * 	返回/views/purchase/purchase_search.ftl
     */
    @RequestMapping("searchPage")
    public String searchPage() {
        return "purchase/purchase_search";
    }
    
    /**
     * 	进货单据列表（分页查询）
     * 	前端：/purchase/searchPage 页面加载时会请求 /purchase/list?page=1&limit=10
     * 	返回 layui 表格要求的 JSON 格式
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> purchaseList(PurchaseListQuery purchaseListQuery) {
        return purchaseListService.purchaseList(purchaseListQuery);
    }
    
    /**
     * 	删除进货单
     * 	前端：点击删除按钮，请求 /purchase/delete?id=xxx
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id) {
        purchaseListService.deletePurchaseList(id);
        return RespBean.success("删除成功");
    }
    
    /**
     * 	商品采购统计接口
     * 	前端统计报表页面count/purchase.ftl请求 /purchase/countPurchase
     */
    @RequestMapping("countPurchase")
    @ResponseBody
    public Map<String, Object> countPurchase(PurchaseListGoodsQuery query) {
    	Map<String, Object> result = purchaseListGoodsService.countPurchase(query);
    	System.out.println("=== countPurchase 返回结果 ===");
        System.out.println(result);
        return result;
    }
}
