package com.edu.seiryo.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.pojo.Supplier;
import com.edu.seiryo.admin.query.SupplierQuery;

import java.util.Map;

/**
 * 供应商服务类
 * 继承 IService 后，自动拥有 save、update、getById、list、page 等方法
 * @author TianTian
 * @date 2022/1/19 13:59
 */
public interface SupplierService extends IService<Supplier> {
	/**
     * 分页查询供应商列表
     * 前端对应：/supplier/list 接口，返回 layui 表格需要的 JSON
     * @param query 查询条件（包含 page, limit, supplierName）
     * @return 分页结果
     */
    Map<String, Object> supplierList(SupplierQuery query);
    
    /**
     * 保存供应商（新增）
     * 前端对应：点击"添加" → 填写表单 → 点击"确认" → 请求 /supplier/save
     * @param supplier 供应商对象
     */
    void saveSupplier(Supplier supplier);
    
    /**
     * 更新供应商（修改）
     * 前端对应：点击"编辑" → 修改表单 → 点击"确认" → 请求 /supplier/update
     * @param supplier 供应商对象
     */
    void updateSupplier(Supplier supplier);
    
    /**
     * 删除供应商（支持批量）
     * 前端对应：勾选多个 → 点击"删除" → 请求 /supplier/delete?ids=1,2,3
     * @param ids 供应商ID数组
     */
    void deleteSupplier(Integer[] ids);
}
