package com.edu.seiryo.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.edu.seiryo.admin.model.RespBean;
import com.edu.seiryo.admin.model.SaleCount;
import com.edu.seiryo.admin.pojo.PurchaseListGoods;
import com.edu.seiryo.admin.pojo.SaleList;
import com.edu.seiryo.admin.pojo.SaleListGoods;
import com.edu.seiryo.admin.query.PurchaseListQuery;
import com.edu.seiryo.admin.query.SaleListQuery;
import com.edu.seiryo.admin.service.SaleListGoodsService;
import com.edu.seiryo.admin.service.SaleListService;
import com.edu.seiryo.admin.service.UserService;
import com.edu.seiryo.admin.utils.AssertUtil;
import com.edu.seiryo.admin.utils.DateUtil;
import com.edu.seiryo.admin.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售单表控制器
 * @author TianTian
 * @date 2022/1/19 23:02
 */
@Controller
@RequestMapping("/sale")
public class SaleListController {

    @Resource
    private SaleListService saleListService;

    @Resource
    private UserService userService;

    @Resource
    private SaleListGoodsService saleListGoodsService;


    /**
     * 销售出库主页
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("saleNumber",saleListService.getNextSaleNumber());
        return "sale/sale";
    }


    @RequestMapping("save")
    @ResponseBody
    public RespBean save(SaleList saleList, String goodsJson, Principal principal){
        String userName = principal.getName();
        saleList.setUserId(userService.findForName(userName).getId());
        Gson gson = new Gson();
        List<SaleListGoods> slgList = gson.fromJson(goodsJson,new TypeToken<List<SaleListGoods>>(){}.getType());
        saleListService.saveSaleList(saleList,slgList);
        return RespBean.success("商品销售出库成功!");
    }

    /**
     * 销售单查询页
     * @return
     */
    @RequestMapping("searchPage")
    public String searchPage(){
        return "sale/sale_search";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> saleList(SaleListQuery saleListQuery){
        return saleListService.saleList(saleListQuery);
    }
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id){
        saleListService.deletesaleList(id);
        return RespBean.success("删除成功");
    }

    @RequestMapping("countSale")
    @ResponseBody
    public Map<String,Object> countSale(SaleListQuery saleListQuery){
        return saleListService.countSale(saleListQuery);
    }



    @RequestMapping("countSaleByDay")
    @ResponseBody
    public Map<String,Object> countDaySale(String begin,String end){
        Map<String,Object> result =new HashMap<String,Object>();
        List<SaleCount> saleCounts =new ArrayList<SaleCount>();
        /**
         * 2021-03-15  -  2021-03-30
         */
        List<Map<String,Object>> list = saleListService.countDaySale(begin,end);
        /**
         * 根据传入的时间段 生成日期列表
         */
        List<String> datas = DateUtil.getRangeDates(begin,end);
        for (String data : datas) {
            SaleCount saleCount =new SaleCount();
            saleCount.setDate(data);
            boolean flag =true;
            for(Map<String,Object> map:list){
                String dd = map.get("saleDate").toString().substring(0,10);
                if(data.equals(dd)){
                    saleCount.setAmountCost(MathUtil.format2Bit(Float.parseFloat(map.get("amountCost").toString())));
                    saleCount.setAmountSale(MathUtil.format2Bit(Float.parseFloat(map.get("amountSale").toString())));
                    saleCount.setAmountProfit(MathUtil.format2Bit(saleCount.getAmountSale()-saleCount.getAmountCost()));
                    flag =false;
                }
            }
            if(flag){
                saleCount.setAmountProfit(0F);
                saleCount.setAmountSale(0F);
                saleCount.setAmountCost(0F);
            }
            saleCounts.add(saleCount);
        }

        result.put("count",saleCounts.size());
        result.put("data",saleCounts);
        result.put("code",0);
        result.put("msg","");
        return result;
    }

    /**
     * 	月销售统计接口
     * 	前端 month.sale.js 请求 /sale/countSaleByMonth
     * @param begin 开始月份 yyyy-MM
     * @param end 结束月份 yyyy-MM
     * @return 标准 Layui 表格格式 {code:0, msg:"", data:[...]}
     */
    @RequestMapping("countSaleByMonth")
    @ResponseBody
    public Map<String, Object> countSaleByMonth(String begin, String end) {
    	List<Map<String, Object>> list = saleListGoodsService.countSaleByMonth(begin, end);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        List<Map<String, Object>> dataList = list != null ? list : new ArrayList<>();
        result.put("data", dataList);
        // 补上总行数
        result.put("count", dataList.size());
        return result;
    }
}
