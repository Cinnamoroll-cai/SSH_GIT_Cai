package com.edu.seiryo.admin.query;

import lombok.Data;

/**
 * 入库查询
 * @author TianTian
 * @date 2022/1/19 13:52
 */
@Data
public class PurchaseListGoodsQuery extends BaseQuery{

    private Integer purchaseListId;
    
    private String goodsName;         // 商品名称（模糊查询）
    private Integer typeId;           // 商品分类ID
    private String startDate;         // 开始日期
    private String endDate;           // 结束日期
}
