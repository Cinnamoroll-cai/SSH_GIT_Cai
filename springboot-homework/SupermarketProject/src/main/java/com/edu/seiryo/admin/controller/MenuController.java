package com.edu.seiryo.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.edu.seiryo.admin.dto.TreeDto;
import com.edu.seiryo.admin.model.RespBean;
import com.edu.seiryo.admin.pojo.Menu;
import com.edu.seiryo.admin.service.MenuService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 菜单控制器
 * @author TianTian
 * @date 2022/1/14 15:40
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
	 @Resource
	 private MenuService menuService;
	 
 	/**
     * 进入菜单管理主页
     * 前端菜单：data-tab="menu/index" → 请求 /menu/index
     */
    @RequestMapping("index")
    public String index() {
        return "menu/menu";
    }

    /**
     * 查询所有菜单（树形表格数据）
     * 前端 menu.js 请求 /menu/list
     * 返回扁平列表，treetable 根据 pId 自动构建树
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list() {
        List<Menu> list = menuService.listAll();
        Map<String,Object> result = new HashMap<>();
        result.put("code",0);
        result.put("msg","");
        result.put("data",list);
        result.put("count",list.size());
        return result;
    }

    /**
     * 进入添加菜单页面
     * 前端 menu.js 点击"添加子项"或"添加"按钮
     * 请求路径：/menu/addMenuPage?grade=xxx&pId=xxx
     */
    @RequestMapping("addMenuPage")
    public String addMenuPage(Integer grade, Integer pId, Model model) {
        model.addAttribute("grade", grade);
        model.addAttribute("pId", pId);
        return "menu/add";
    }

    /**
     * 进入更新菜单页面
     * 前端 menu.js 点击"修改"按钮
     * 请求路径：/menu/updateMenuPage?id=xxx
     */
    @RequestMapping("updateMenuPage")
    public String updateMenuPage(Integer id, Model model) {
        Menu menu = menuService.getById(id);
        model.addAttribute("menu", menu);
        return "menu/update";
    }

    /**
     * 保存菜单（新增）
     * 前端 menu.add.js 提交 /menu/save
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean save(Menu menu) {
        menuService.saveMenu(menu);
        return RespBean.success("添加菜单成功");
    }

    /**
     * 更新菜单
     * 前端 menu.update.js 提交 /menu/update
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Menu menu) {
        menuService.updateMenu(menu);
        return RespBean.success("更新菜单成功");
    }

    /**
     * 删除菜单
     * 前端 menu.js 请求 /menu/delete?id=xxx
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id) {
        menuService.deleteMenu(id);
        return RespBean.success("删除菜单成功");
    }
}
