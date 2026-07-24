package com.edu.seiryo.admin.query;

import lombok.Data;

/**
 * 供应商查询条件对象
 * 前端传递的参数会映射到此对象
 * @author TianTian
 * @date 2022/1/18 17:49
 */
@Data
public class SupplierQuery extends BaseQuery{
	// 继承 BaseQuery 的 page 和 limit
    // 额外添加搜索条件
    private String supplierName; // 供应商名称（模糊查询）
}
