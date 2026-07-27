package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.seiryo.admin.pojo.*;
import com.edu.seiryo.admin.mapper.ReturnListMapper;
import com.edu.seiryo.admin.query.ReturnListQuery;
import com.edu.seiryo.admin.service.GoodsService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.service.ReturnListGoodsService;
import com.edu.seiryo.admin.service.ReturnListService;
import com.edu.seiryo.admin.utils.AssertUtil;
import com.edu.seiryo.admin.utils.DateUtil;
import com.edu.seiryo.admin.utils.PageResultUtil;
import com.edu.seiryo.admin.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.table.TableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 退货单表 服务实现类
 * </p>
 *
 * @author 老李
 */
@Service
public class ReturnListServiceImpl extends ServiceImpl<ReturnListMapper, ReturnList> implements ReturnListService {

    @Resource
    private ReturnListGoodsService returnListGoodsService;

    @Resource
    private GoodsService goodsService;

    @Override
    public String getNextReturnNumber() {
        // TH20210101000X
        try {
            StringBuffer stringBuffer =new StringBuffer();
            stringBuffer.append("TH");
            stringBuffer.append(DateUtil.getCurrentDateStr());
            String returnNumber = this.baseMapper.getNextReturnNumber();
            if(null !=returnNumber){
                stringBuffer.append(StringUtil.formatCode(returnNumber));
            }else{
                stringBuffer.append("0001");
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveReturnList(ReturnList returnList, List<ReturnListGoods> rlgList) {
    	// 1. 参数校验
        AssertUtil.isTrue(returnList.getSupplierId() == 0, "供应商为空");
        AssertUtil.isTrue(returnList.getAmountPayable() == null, "应付金额不能为空");
        AssertUtil.isTrue(returnList.getAmountPaid() == null, "实付金额不能为空");
        AssertUtil.isTrue(returnList.getReturnDate() == null, "请选择日期");
        
        // 校验日期只能选今天
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = formatter.format(new Date());
        String returnDateStr = formatter.format(returnList.getReturnDate());
        AssertUtil.isTrue(!returnDateStr.equals(todayStr), "请选择本日时间");
        
        // 2. 保存主表（获取主键 ID）
        boolean saved = this.save(returnList);
        AssertUtil.isTrue(!saved, "退货单主表保存失败");
        
        // 3. 获取刚插入的主表记录（获取 ID）
        ReturnList temp = this.getOne(new QueryWrapper<ReturnList>().eq("return_number", returnList.getReturnNumber()));
        AssertUtil.isTrue(temp == null, "获取退货单记录失败");
        Integer returnListId = temp.getId();
        
        // 4. 保存明细
        AssertUtil.isTrue(rlgList == null || rlgList.isEmpty(), "请选择商品");
        for (ReturnListGoods rlg : rlgList) {
            // 设置关联主表 ID
            rlg.setReturnListId(returnListId);
            // 如果总价为 null，自动计算
            if (rlg.getTotal() == null) {
                rlg.setTotal(rlg.getPrice() * rlg.getNum());
            }
            // 保存明细
            boolean detailSaved = returnListGoodsService.save(rlg);
            AssertUtil.isTrue(!detailSaved, "商品明细保存失败");
        }
        
        // 5. 更新库存（减少）
        for (ReturnListGoods rlg : rlgList) {
            Goods goods = goodsService.getById(rlg.getGoodsId());
            if (goods != null) {
                goods.setInventoryQuantity(goods.getInventoryQuantity() - rlg.getNum());
                goods.setState(2);
                goodsService.updateById(goods);
            }
        }
    }

    @Override
    public Map<String, Object> returnList(ReturnListQuery returnListQuery) {
        IPage<ReturnList> page = new Page<ReturnList>(returnListQuery.getPage(),returnListQuery.getLimit());
        page =  this.baseMapper.returnList(page,returnListQuery);
        return PageResultUtil.setResult(page.getTotal(),page.getRecords());
    }

    @Override
    public void deleteReturnList(Integer id) {
        /**
         * 1.退货单商品记录删除
         * 2.退货单记录删除
         */
        AssertUtil.isTrue(!(returnListGoodsService.remove(new QueryWrapper<ReturnListGoods>().eq("return_list_id",id))),
                "记录删除失败!");
        AssertUtil.isTrue(!(this.removeById(id)),"记录删除失败!");
    }
}
