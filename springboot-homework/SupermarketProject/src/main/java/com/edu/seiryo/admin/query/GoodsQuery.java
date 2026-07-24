package com.edu.seiryo.admin.query;

import lombok.Data;

import java.util.List;

@Data
public class GoodsQuery extends BaseQuery{
    private String goodsName;// 商品名称（模糊查询）
    private Integer typeId;// 商品分类ID

    private List<Integer> typeIds;

    // 查询类型 区分库存量是否大于0查询
    /**
     * 1 库存量=0
     * 2 库存量>0
     */
    private Integer type;

}