package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.seiryo.admin.pojo.DamageListGoods;
import com.edu.seiryo.admin.pojo.OverflowListGoods;
import com.edu.seiryo.admin.mapper.OverflowListGoodsMapper;
import com.edu.seiryo.admin.query.DamageListGoodsQuery;
import com.edu.seiryo.admin.query.OverflowListGoodsQuery;
import com.edu.seiryo.admin.service.OverflowListGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 报溢单商品表服务类
 * @author TianTian
 * @date 2022/1/21 14:02
 */
@Service
public class OverflowListGoodsServiceImpl extends ServiceImpl<OverflowListGoodsMapper, OverflowListGoods> implements OverflowListGoodsService {

    @Override
    public Map<String, Object> overflowListGoodsList(OverflowListGoodsQuery overflowListGoodsQuery) {
        IPage<OverflowListGoods> page = new Page<OverflowListGoods>(overflowListGoodsQuery.getPage(),overflowListGoodsQuery.getLimit());
        QueryWrapper<OverflowListGoods> queryWrapper =new QueryWrapper<OverflowListGoods>();
        if(null != overflowListGoodsQuery.getOverflowListId()){
            queryWrapper.eq("overflow_list_id",overflowListGoodsQuery.getOverflowListId());
        }
        page =  this.baseMapper.selectPage(page,queryWrapper);
        return PageResultUtil.setResult(page.getTotal(),page.getRecords());
    }
}
