package com.edu.seiryo.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.edu.seiryo.admin.model.RespBean;
import com.edu.seiryo.admin.pojo.Supplier;
import com.edu.seiryo.admin.query.SupplierQuery;
import com.edu.seiryo.admin.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
	@Resource
    private SupplierService supplierService;
	
	/**
	 * 首页点击“供应商管理”跳转
	 * 前端菜单：data-tab="supplier/index" → 请求 /supplier/index
	 * 返回视图：/views/supplier/supplier.ftl
	 * @return /supplier/supplier
	 */
    @RequestMapping("index")
    public String index(){
        return "/supplier/supplier";
    }
    
    /**
	 * 前端supplier.ftl的supplier.js请求的接口是：/supplier/list
	 * 请求路径：/supplier/list?page=1&limit=10&supplierName=xxx
	 * 后端 Controller 方法，路径为 /supplier/list，接收分页参数，返回 JSON 数据
	 * "code": 0,
  		"msg": "",
  		"count": 总记录数,
  		"data": [供应商列表]
  	 * 	@return layui 表格要求的 JSON
	 */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(SupplierQuery supplierQuery) {
    	return supplierService.supplierList(supplierQuery);
    }
    
    /**
     * 进入新增/修改供应商页面
     * 前端：点击"添加"或"编辑"按钮时调用
     * 	添加：/supplier/addOrUpdateSupplierPage（不带 id）
     * 	编辑：/supplier/addOrUpdateSupplierPage?id=1（带 id）
     * 返回视图：/views/supplier/add_update.ftl
     */
    @RequestMapping("addOrUpdateSupplierPage")
    public String addOrUpdateSupplierPage(Integer id, Model model) {
        if (id != null) {
            // 编辑根据 ID 查询供应商信息，回显到表单
            Supplier supplier = supplierService.getById(id);
            model.addAttribute("supplier", supplier);
        }
        return "supplier/add_update";
    }

    /**
     * 保存供应商（新增）
     * 前端 add_update.ftl 表单提交 → POST /supplier/save
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean save(Supplier supplier) {
        supplierService.saveSupplier(supplier);
        return RespBean.success("添加供应商成功");
    }

    /**
     * 更新供应商（修改）
     * 前端 add_update.ftl 表单提交（带 id） → POST /supplier/update
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Supplier supplier) {
        supplierService.updateSupplier(supplier);
        return RespBean.success("更新供应商成功");
    }

    /**
     * 删除供应商（支持批量）
     * 前端：勾选 → 点击"删除" → GET /supplier/delete?ids=1,2,3
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer[] ids) {
        supplierService.deleteSupplier(ids);
        return RespBean.success("删除供应商成功");
    }

    /**
     * 查询所有供应商（用于下拉框）
     * 前端：进货入库页面需要下拉选择供应商 → 请求 /supplier/allGoodsSuppliers
     * 返回：所有未删除的供应商列表
     */
    @RequestMapping("allGoodsSuppliers")
    @ResponseBody
    public List<Supplier> allSuppliers() {
        return supplierService.list();
    }
}
