package com.edu.seiryo.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seiryo.admin.pojo.Supplier;
import sun.print.BackgroundServiceLookup;
/**
 * 供应商 Mapper 接口
 * 继承 BaseMapper 后，自动拥有增删改查方法，无需写 XML
 */
@Mapper
@Repository
public interface SupplierMapper extends BaseMapper<Supplier>{
	// 目前不需要额外方法，BaseMapper 已提供：
    // - selectList() 查询列表
    // - selectPage() 分页查询
    // - insert() 插入
    // - updateById() 更新
    // - deleteById() 删除
    // - deleteBatchIds() 批量删除
}
