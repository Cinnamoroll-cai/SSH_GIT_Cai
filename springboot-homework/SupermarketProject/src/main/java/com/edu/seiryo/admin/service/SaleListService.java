package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.pojo.SaleList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.pojo.SaleListGoods;
import com.edu.seiryo.admin.query.SaleListQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售单表 服务类
 * </p>
 *
 * @author 老李
 */
public interface SaleListService extends IService<SaleList> {

    String getNextSaleNumber();

    void saveSaleList(SaleList saleList, List<SaleListGoods> slgList);

    Map<String, Object> saleList(SaleListQuery saleListQuery);

    void deletesaleList(Integer id);

    Map<String, Object> countSale(SaleListQuery saleListQuery);

    List<Map<String, Object>> countDaySale(String begin, String end);

}
