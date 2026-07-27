package com.edu.seiryo.admin.mapper;

import com.edu.seiryo.admin.pojo.PurchaseListGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 	进货单商品表 Mapper 接口
 * </p>
 *
 * @author 老李
 * @since 2021-03-27
 */
@Mapper
@Repository
public interface PurchaseListGoodsMapper extends BaseMapper<PurchaseListGoods> {
	 /**
     * 	商品采购统计查询
     * 	（返回列表，分页由 PageHelper 处理）
     * @param goodsName 商品名称（模糊查询）
     * @param typeId 商品分类ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果
     */
	IPage<Map<String, Object>> countPurchase(Page<?> page,
            @Param("goodsName") String goodsName,
            @Param("typeId") Integer typeId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

}
