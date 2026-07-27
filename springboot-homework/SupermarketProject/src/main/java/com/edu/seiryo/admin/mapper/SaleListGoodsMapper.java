package com.edu.seiryo.admin.mapper;

import com.edu.seiryo.admin.pojo.SaleListGoods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 销售单商品表 Mapper 接口
 * </p>
 *
 * @author 老李
 */
@Mapper
@Repository
public interface SaleListGoodsMapper extends BaseMapper<SaleListGoods> {
	/**
     * 	月销售统计（按月份汇总）
     * @param begin 开始月份 yyyy-MM
     * @param end 结束月份 yyyy-MM
     * @return 每个月的数据：date, amountCost, amountSale, amountProfit
     */
    List<Map<String, Object>> countSaleByMonth(@Param("begin") String begin, @Param("end") String end);
}
