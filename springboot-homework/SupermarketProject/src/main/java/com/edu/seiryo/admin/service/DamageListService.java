package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.pojo.DamageList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.pojo.DamageListGoods;
import com.edu.seiryo.admin.query.DamageListQuery;

import java.util.List;
import java.util.Map;

/**
 * 报损单服务类
 * @author TianTian
 * @date 2022/1/21 13:42
 */
public interface DamageListService extends IService<DamageList> {

    String getNextDamageNumber();

    void saveDamageList(DamageList damageList, List<DamageListGoods> plgList);

    Map<String, Object> damageList(DamageListQuery damageListQuery);

    void deletedamageList(Integer id);
}
