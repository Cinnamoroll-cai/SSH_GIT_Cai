package com.edu.seiryo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.seiryo.admin.pojo.Goods;
import com.edu.seiryo.admin.query.GoodsQuery;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * 商品 Mapper 接口
 * 继承 BaseMapper 自动拥有增删改查方法
 */
@Mapper
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {
	// BaseMapper 已提供：
    // - selectPage() 分页查询
    // - insert() 插入
    // - updateById() 更新
    // - deleteById() 删除
    // - selectList() 查询列表
	
	 /**
     * 库存查询（分页）
     * 包含：商品信息 + 分类名称 + 单位名称 + 销售总数
     */
    IPage<Goods> stockList(Page<Goods> page, 
                           @Param("goodsName") String goodsName, 
                           @Param("typeId") Integer typeId);
}
