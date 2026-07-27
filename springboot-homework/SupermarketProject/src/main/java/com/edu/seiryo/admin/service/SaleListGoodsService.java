package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.pojo.SaleListGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.query.saleListGoodsQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售单商品表 服务类
 * </p>
 *
 * @author 老李
 */
public interface SaleListGoodsService extends IService<SaleListGoods> {

    Integer getSaleTotalByGoodsId(Integer id);

    Map<String, Object> saleListGoodsList(saleListGoodsQuery saleListGoodsQuery);
    
    /**
     * 	月销售统计
     */
    List<Map<String, Object>> countSaleByMonth(String begin, String end);


}
