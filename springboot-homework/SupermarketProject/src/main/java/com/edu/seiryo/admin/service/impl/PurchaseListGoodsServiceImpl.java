package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.seiryo.admin.pojo.PurchaseListGoods;
import com.edu.seiryo.admin.mapper.PurchaseListGoodsMapper;
import com.edu.seiryo.admin.pojo.User;
import com.edu.seiryo.admin.query.PurchaseListGoodsQuery;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.service.PurchaseListGoodsService;
import com.edu.seiryo.admin.utils.PageResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进货单商品表 服务实现类
 * </p>
 *
 * @author 老李
 */
@Service
public class PurchaseListGoodsServiceImpl extends ServiceImpl<PurchaseListGoodsMapper, PurchaseListGoods> implements PurchaseListGoodsService {
	/**
     * 	根据进货单ID查询明细列表（分页）
     * 	前端点击进货单行的“货单”按钮，会请求 /purchaseListGoods/list?purchaseListId=xxx
     */
    @Override
    public Map<String, Object> purchaseListGoodsList(PurchaseListGoodsQuery query) {
    	// 1. 构建 MyBatis-Plus 分页对象
        Page<PurchaseListGoods> page = new Page<>(query.getPage(), query.getLimit());
        
        // 2. 构建查询条件
        QueryWrapper<PurchaseListGoods> wrapper = new QueryWrapper<>();
        if (query.getPurchaseListId() != null) {
            wrapper.eq("purchase_list_id", query.getPurchaseListId());
        }
        // 按主键降序（可选）
        wrapper.orderByDesc("id");
        
        // 3. 执行分页查询
        /*
         * 调用的 this.baseMapper.selectList(wrapper)，
         * 实际上就是调用了 BaseMapper 自带的 selectList 方法，
         * 不需要自己写 XML 或 Mapper 接口中的方法
         */
        IPage<PurchaseListGoods> iPage = this.baseMapper.selectPage(page, wrapper);
        
        // 4. 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", iPage.getTotal());
        result.put("data", iPage.getRecords());
        return result;
    }
}
