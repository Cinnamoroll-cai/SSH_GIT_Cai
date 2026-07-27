package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.dto.TreeDto;
import com.edu.seiryo.admin.pojo.GoodsType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商品类别表单服务类
 * @author TianTian
 * @date 2022/1/19 13:56
 */
public interface GoodsTypeService extends IService<GoodsType> {
	/**
     * 	查询所有商品分类（树形结构）
     * 	前端商品选择弹窗goods.ftl左侧分类树
     * 	返回格式：[{id:1, name:"分类名", children:[...]}]
     */
	List<Map<String, Object>> queryAllGoodsType();
	
	/**
     * 	保存商品分类
     */
    void saveGoodsType(GoodsType goodsType);

    /**
     * 	删除商品分类（检查是否有子节点）
     */
    void deleteGoodsType(Integer id);
}
