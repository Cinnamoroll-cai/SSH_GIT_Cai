package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.seiryo.admin.pojo.Goods;
import com.edu.seiryo.admin.pojo.GoodsType;
import com.edu.seiryo.admin.pojo.GoodsUnit;
import com.edu.seiryo.admin.mapper.GoodsMapper;
import com.edu.seiryo.admin.mapper.GoodsTypeMapper;
import com.edu.seiryo.admin.mapper.GoodsUnitMapper;
import com.edu.seiryo.admin.query.GoodsQuery;
import com.edu.seiryo.admin.service.CustomerReturnListGoodsService;
import com.edu.seiryo.admin.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.service.GoodsTypeService;
import com.edu.seiryo.admin.service.SaleListGoodsService;
import com.edu.seiryo.admin.utils.AssertUtil;
import com.edu.seiryo.admin.utils.PageResultUtil;
import com.edu.seiryo.admin.utils.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品表实现类
 * @author TianTian
 * @date 2022/1/19 14:51
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
	
	/*
	 * Goods 实体类中有 typeName 和 unitName 字段（标记了 @TableField(exist = false)）
	 * 需要关联查询 t_goods_type 和 t_goods_unit 来填充这两个字段
	 */
	@Resource
    private GoodsTypeMapper goodsTypeMapper;  // 注入分类Mapper

    @Resource
    private GoodsUnitMapper goodsUnitMapper;  // 注入单位Mapper

	/**
	 * 添加进货商品页面展示商品表
	 */
	@Override
	public Map<String, Object> goodslist(GoodsQuery query) {
		// TODO Auto-generated method stub
		// 1. 构建分页对象
        Page<Goods> page = new Page<>(query.getPage(), query.getLimit());

        // 2. 构建查询条件
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DEL", 0);
        if (!StringUtil.isEmpty(query.getGoodsName())) {
            wrapper.like("NAME", query.getGoodsName());
        }
        if (query.getTypeId() != null && query.getTypeId() > 0) {
            wrapper.eq("TYPE_ID", query.getTypeId());
        }
        wrapper.orderByDesc("ID");

        // 3. 执行分页查询
        IPage<Goods> resultPage = this.baseMapper.selectPage(page, wrapper);

        // 4. 填充分类名称和单位名称
        List<Goods> goodsList = resultPage.getRecords();
        for (Goods goods : goodsList) {
            // 填充分类名称
            if (goods.getTypeId() != null) {
                GoodsType type = goodsTypeMapper.selectById(goods.getTypeId());
                if (type != null) {
                    goods.setTypeName(type.getName());
                }
            }
            // 填充单位名称（注意：数据库中 unit 存储的是单位ID，需要转为名称）
            if (!StringUtil.isEmpty(goods.getUnit())) {
                try {
                    Integer unitId = Integer.parseInt(goods.getUnit());
                    GoodsUnit unit = goodsUnitMapper.selectById(unitId);
                    if (unit != null) {
                        goods.setUnitName(unit.getName());
                    }
                } catch (NumberFormatException e) {
                    // 如果 unit 不是数字，可能是直接存了名称，直接赋值
                    goods.setUnitName(goods.getUnit());
                }
            }
        }

        // 5. 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", resultPage.getTotal());
        result.put("data", goodsList);
        return result;
    }


}
