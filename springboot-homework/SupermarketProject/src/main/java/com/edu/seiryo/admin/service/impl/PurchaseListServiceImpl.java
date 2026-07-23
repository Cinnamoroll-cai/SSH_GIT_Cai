package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.seiryo.admin.model.CountResultModel;
import com.edu.seiryo.admin.pojo.Goods;
import com.edu.seiryo.admin.pojo.PurchaseList;
import com.edu.seiryo.admin.mapper.PurchaseListGoodsMapper;
import com.edu.seiryo.admin.mapper.PurchaseListMapper;
import com.edu.seiryo.admin.pojo.PurchaseListGoods;
import com.edu.seiryo.admin.query.PurchaseListQuery;
import com.edu.seiryo.admin.service.GoodsService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.service.GoodsTypeService;
import com.edu.seiryo.admin.service.PurchaseListGoodsService;
import com.edu.seiryo.admin.service.PurchaseListService;
import com.edu.seiryo.admin.utils.AssertUtil;
import com.edu.seiryo.admin.utils.DateUtil;
import com.edu.seiryo.admin.utils.PageResultUtil;
import com.edu.seiryo.admin.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进货单 服务实现类
 * </p>
 *
 * @author 老李
 */
@Service
public class PurchaseListServiceImpl extends ServiceImpl<PurchaseListMapper, PurchaseList> implements PurchaseListService {
	@Resource
    private PurchaseListMapper purchaseListMapper;

    @Resource
    private PurchaseListGoodsMapper purchaseListGoodsMapper;
    
    /**
     * 	生成进货单号
     * 	前端加载时，/purchase/index 调用此方法，将单号放入 model，前端显示在“单号”处
     */
    @Override
	public String getNextPurchaseNumber() {
    	// 获取当前日期字符串 yyyyMMdd
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 查询今天最大单号
        String max = purchaseListMapper.getMaxPurchaseNumberToday(today);
        if (StringUtil.isEmpty(max)) {
            // 今天没有单号，从 0001 开始
            return "JH" + today + "0001";
        }
        // 取出最后4位序号，加1，并格式化
        String suffix = max.substring(max.length() - 4);
        int seq = Integer.parseInt(suffix) + 1;
        return "JH" + today + String.format("%04d", seq);
	}

    /**
     * 	保存进货单
     * 	前端对应：点击“保存”按钮，提交表单数据（主表 + 商品JSON字符串）
     * 	事务注解保证主表和明细表同时成功或失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void savePurchaseList(PurchaseList purchaseList, List<PurchaseListGoods> goodsList) {
    	// 1. 保存主表
        // 设置默认值：如果不设置，前端可能传空
        if (purchaseList.getState() == null) {
            purchaseList.setState(1); // 1-已付款，0-未付款
        }
        // 调用 MyBatis-Plus 的 save 方法（继承自 IService）
        boolean saved = this.save(purchaseList);  // 会执行 INSERT
        AssertUtil.isTrue(!saved, "进货单主表保存失败");

        // 2. 保存明细表
        // 每一条明细都要关联主表的 id
        for (PurchaseListGoods goods : goodsList) {
            goods.setPurchaseListId(purchaseList.getId()); // 关联主表ID
            // 计算总价（如果前端没有传，可以自己算）
            if (goods.getTotal() == null) {
                goods.setTotal(goods.getPrice() * goods.getNum());
            }
            // 插入明细
            int result = purchaseListGoodsMapper.insert(goods);
            AssertUtil.isTrue(result != 1, "商品明细保存失败");
        }
		
	}
    
    /**
     * 	分页查询进货单列表
     * 	前端进货单据查询页面 /purchase/searchPage 请求 /purchase/list 接口
     * 	使用 PageHelper 进行分页
     */
	@Override
	public Map<String, Object> purchaseList(PurchaseListQuery query) {
		// 开启分页（PageHelper 自动拦截后续的 SQL）
        PageHelper.startPage(query.getPage(), query.getLimit());
        // 构建查询条件
        QueryWrapper<PurchaseList> wrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(query.getPurchaseNumber())) {
            wrapper.like("purchase_number", query.getPurchaseNumber());
        }
        if (query.getSupplierId() != null && query.getSupplierId() > 0) {
            wrapper.eq("supplier_id", query.getSupplierId());
        }
        if (query.getState() != null) {
            wrapper.eq("state", query.getState());
        }
        // 日期范围查询（假设 query 中有 startDate 和 endDate）
        // ... 可按需扩展
        wrapper.orderByDesc("id"); // 按主键降序

        // 执行查询，返回 PageInfo 对象
        List<PurchaseList> list = purchaseListMapper.selectList(wrapper);
        PageInfo<PurchaseList> pageInfo = new PageInfo<>(list);

        // 构造返回结果（layui 表格要求的数据格式）
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", pageInfo.getTotal()); // 总记录数
        result.put("data", pageInfo.getList());    // 当前页数据
        return result;
	}
	
	/**
     * 	删除进货单（逻辑删除或物理删除，这里做物理删除）
     * 	前端点击删除按钮，请求 /purchase/delete?id=xxx
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void deletePurchaseList(Integer id) {
		// TODO Auto-generated method stub
    	 // 先删除明细
        QueryWrapper<PurchaseListGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("purchase_list_id", id);
        purchaseListGoodsMapper.delete(wrapper); // 删除该单所有明细
        // 再删除主表
        int result = purchaseListMapper.deleteById(id);
        AssertUtil.isTrue(result != 1, "删除进货单失败");
	}



}
