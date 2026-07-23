package com.edu.seiryo.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.edu.seiryo.admin.model.RespBean;
import com.edu.seiryo.admin.pojo.DamageList;
import com.edu.seiryo.admin.pojo.DamageListGoods;
import com.edu.seiryo.admin.pojo.OverflowList;
import com.edu.seiryo.admin.pojo.OverflowListGoods;
import com.edu.seiryo.admin.query.DamageListQuery;
import com.edu.seiryo.admin.query.OverFlowListQuery;
import com.edu.seiryo.admin.service.OverflowListService;
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
 * @author TianTian
 * @date 2022/1/21 13:39
 */
@Controller
@RequestMapping("/overflow")
public class OverflowListController {

    @Resource
    private OverflowListService overflowListService;

    @Resource
    private UserService userService;

    /**
     * 商品报溢主页
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("overflowNumber",overflowListService.getOverflowNumber());
        return "overflow/overflow";
    }


    @RequestMapping("save")
    @ResponseBody
    public RespBean save(OverflowList overflowList, String goodsJson, Principal principal){
        String userName = principal.getName();
        overflowList.setUserId(userService.findForName(userName).getId());
        Gson gson = new Gson();
        List<OverflowListGoods> plgList = gson.fromJson(goodsJson,new TypeToken<List<OverflowListGoods>>(){}.getType());
        overflowListService.saveOverflowList(overflowList,plgList);
        return RespBean.success("商品报溢入库成功!");
    }


    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> overFlowList(OverFlowListQuery overFlowListQuery){
        return overflowListService.overFlowList(overFlowListQuery);
    }
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id){
        overflowListService.deleteoverflowList(id);
        return RespBean.success("删除成功");
    }



}
