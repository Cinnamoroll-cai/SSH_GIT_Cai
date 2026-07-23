package com.edu.seiryo.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.edu.seiryo.admin.model.CountResultModel;
import com.edu.seiryo.admin.pojo.PurchaseList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seiryo.admin.query.PurchaseListQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 进货单接口
 * @author TianTian
 * @date 2022/1/21 18:27
 */
@Mapper
public interface PurchaseListMapper extends BaseMapper<PurchaseList> {
	/**
     * 	查询今天已生成的最大进货单号（用于生成新单号）
     * 	前端页面加载时调用 /purchase/index，后端需要生成单号并显示在页面“单号”处
     * 	@return 返回最大单号，例如 "JH202603210003"
     */
    String getMaxPurchaseNumberToday(@Param("today") String today); // 入参为 yyyyMMdd
}
