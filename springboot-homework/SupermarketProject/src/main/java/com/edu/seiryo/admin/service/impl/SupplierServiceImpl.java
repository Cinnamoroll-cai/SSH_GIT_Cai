package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.mapper.SupplierMapper;
import com.edu.seiryo.admin.pojo.Supplier;
import com.edu.seiryo.admin.query.SupplierQuery;
import com.edu.seiryo.admin.service.SupplierService;
import com.edu.seiryo.admin.utils.AssertUtil;
import com.edu.seiryo.admin.utils.PageResultUtil;
import com.edu.seiryo.admin.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商服务类
 * @author TianTian
 * @date 2022/1/19 14:43
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {
	 /**
     * 分页查询供应商列表
     * 前端对应：supplier.js 初始化表格时，会请求 /supplier/list
     * 返回格式必须符合 layui 表格要求
     */
	@Override
	public Map<String, Object> supplierList(SupplierQuery query) {
		// 用 MyBatis-Plus 自带的分页
		// 1. 构建分页对象
        Page<Supplier> page = new Page<>(query.getPage(), query.getLimit());

        // 2. 构建查询条件
        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();
        // 只查询未删除的供应商（is_del = 0）
        wrapper.eq("IS_DEL", 0);
        // 如果传入了供应商名称，进行模糊查询
        if (!StringUtil.isEmpty(query.getSupplierName())) {
            wrapper.like("NAME", query.getSupplierName());
        }
        // 按主键降序排列（最新的在前面）
        wrapper.orderByDesc("ID");

        // 3. 执行查询
        // 执行分页查询
        IPage<Supplier> resultPage = this.baseMapper.selectPage(page, wrapper);

        // 5. 构造 layui 表格需要的 JSON 格式
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);          // 0 表示成功
        result.put("msg", "");           // 错误信息
        result.put("count", resultPage.getTotal()); // 总记录数
        result.put("data", resultPage.getRecords());   // 当前页数据

        return result;
	}
	
	/**
     * 保存供应商（新增）
     * 前端对应：点击"添加" → 弹出 add_update.ftl → 填写表单 → 点击"确认"
     * 请求路径：/supplier/save
     */
	@Override
	public void saveSupplier(Supplier supplier) {
		// TODO Auto-generated method stub
		// 1. 参数校验（供应商名称不能为空）
        AssertUtil.isTrue(StringUtil.isEmpty(supplier.getName()), "供应商名称不能为空");
        // 2. 设置默认值：未删除
        supplier.setIsDel(0);
        // 3. 执行插入
        boolean result = this.save(supplier);
        AssertUtil.isTrue(!result, "添加供应商失败");
	}
	
	 /**
     * 更新供应商（修改）
     * 前端对应：点击"编辑" → 弹出 add_update.ftl（带数据） → 修改 → 点击"确认"
     * 请求路径：/supplier/update
     */
	@Override
	public void updateSupplier(Supplier supplier) {
		// TODO Auto-generated method stub
		 // 1. 参数校验
        AssertUtil.isTrue(supplier.getId() == null, "供应商ID不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(supplier.getName()), "供应商名称不能为空");
        // 2. 执行更新
        boolean result = this.updateById(supplier);
        AssertUtil.isTrue(!result, "更新供应商失败");
	}
	
	/**
     * 删除供应商（支持批量）
     * 前端对应：勾选 → 点击"删除" → 请求 /supplier/delete?ids=1,2,3
     * 注意：这里是逻辑删除（将 is_del 设为 1），不是物理删除
     */
	@Override
	public void deleteSupplier(Integer[] ids) {
		// TODO Auto-generated method stub
		 // 1. 参数校验
        AssertUtil.isTrue(ids == null || ids.length == 0, "请选择要删除的供应商");
        // 2. 批量逻辑删除：将 is_del 设为 1
        for (Integer id : ids) {
            Supplier supplier = new Supplier();
            supplier.setId(id);
            supplier.setIsDel(1);
            this.updateById(supplier);
        }
	}

}
