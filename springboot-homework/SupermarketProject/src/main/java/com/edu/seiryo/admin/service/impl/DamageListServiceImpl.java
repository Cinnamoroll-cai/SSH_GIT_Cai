package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.seiryo.admin.pojo.*;
import com.edu.seiryo.admin.mapper.DamageListMapper;
import com.edu.seiryo.admin.mapper.UserMapper;
import com.edu.seiryo.admin.query.DamageListQuery;
import com.edu.seiryo.admin.service.DamageListGoodsService;
import com.edu.seiryo.admin.service.DamageListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.service.GoodsService;
import com.edu.seiryo.admin.utils.AssertUtil;
import com.edu.seiryo.admin.utils.DateUtil;
import com.edu.seiryo.admin.utils.PageResultUtil;
import com.edu.seiryo.admin.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 报损表单类
 * @author TianTian
 * @date 2022/1/21 14:00
 */
@Service
public class DamageListServiceImpl extends ServiceImpl<DamageListMapper, DamageList> implements DamageListService {


    @Resource
    private GoodsService goodsService;

    @Resource
    private DamageListGoodsService damageListGoodsService;
    
    @Resource
    private UserMapper userMapper;

    @Override
    public String getNextDamageNumber() {
        try {
            StringBuffer stringBuffer =new StringBuffer();
            stringBuffer.append("BS");
            stringBuffer.append(DateUtil.getCurrentDateStr());
            String saleNumber = this.baseMapper.getNextDamageNumber();
            if(null !=saleNumber){
                stringBuffer.append(StringUtil.formatCode(saleNumber));
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
    public void saveDamageList(DamageList damageList, List<DamageListGoods> plgList) {
        AssertUtil.isTrue(damageList.getDamageDate()==null,"请填写日期");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        AssertUtil.isTrue(0!= formatter.format(damageList.getDamageDate()).compareTo(formatter.format(date)),"请输入今日时间");
        AssertUtil.isTrue(!(this.save(damageList)),"记录添加失败!");
        DamageList temp = this.getOne(new QueryWrapper<DamageList>().eq("damage_number",damageList.getDamageNumber()));
        AssertUtil.isTrue(plgList==null,"请选择商品");
        plgList.forEach(plg->{
            plg.setDamageListId(temp.getId());
            Goods goods = goodsService.getById(plg.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity()-plg.getNum());
            goods.setState(2);
            AssertUtil.isTrue(!(goodsService.updateById(goods)),"记录添加失败!");
            AssertUtil.isTrue(!(damageListGoodsService.save(plg)),"记录添加失败!");
        });
    }

    @Override
    public Map<String, Object> damageList(DamageListQuery damageListQuery) {
        Page<DamageList> page = new Page<>(damageListQuery.getPage(), damageListQuery.getLimit());
        QueryWrapper<DamageList> wrapper = new QueryWrapper<>();
        if(org.springframework.util.StringUtils.hasText(damageListQuery.getStartDate())){
            wrapper.apply("TRUNC(damage_date) >= TO_DATE({0},'yyyy-MM-dd')",damageListQuery.getStartDate());
        }
        if(org.springframework.util.StringUtils.hasText(damageListQuery.getEndDate())){
            wrapper.apply("TRUNC(damage_date) <= TO_DATE({0},'yyyy-MM-dd')",damageListQuery.getEndDate());
        }
        wrapper.orderByDesc("damage_date");
        // 用IPage接收
        IPage<DamageList> pageData = baseMapper.selectPage(page, wrapper);
        List<DamageList> records = pageData.getRecords();

        if(!records.isEmpty()){
            List<Integer> userIds = records.stream()
                    .map(DamageList::getUserId)
                    .collect(Collectors.toList());
            List<User> userList = userMapper.selectBatchIds(userIds);
            Map<Integer,String> userMap = userList.stream()
                    .collect(Collectors.toMap(User::getId,User::getUserName));
            for(DamageList dl : records){
                dl.setUserName(userMap.get(dl.getUserId()));
            }
        }
        return PageResultUtil.setResult(pageData.getTotal(), records);
    }
    @Override
    public void deletedamageList(Integer id) {
        /**
         * 1.报损单商品记录删除
         * 2.报损单记录删除
         */
        AssertUtil.isTrue(!(damageListGoodsService.remove(new QueryWrapper<DamageListGoods>().eq("damage_list_id",id))),
                "记录删除失败!");
        AssertUtil.isTrue(!(this.removeById(id)),"记录删除失败!");
    }
}
