package com.edu.seiryo.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.pojo.GoodsUnit;

import java.util.List;
/**
 * 商品单元表单服务类
 * @author TianTian
 * @date 2022/1/19 13:57
 */
public interface GoodsUnitService extends IService<GoodsUnit> {
	 /**
     * 	查询所有商品单位
     * 	前端添加/编辑商品时下拉选择单位
     * 	请求路径：/goodsUnit/list
     */
    List<GoodsUnit> findAll();
}
