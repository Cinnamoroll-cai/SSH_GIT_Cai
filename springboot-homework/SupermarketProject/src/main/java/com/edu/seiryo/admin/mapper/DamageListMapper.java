package com.edu.seiryo.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.edu.seiryo.admin.pojo.DamageList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seiryo.admin.query.DamageListQuery;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.test.context.ActiveProfiles;

/**
 * 报损单 Mapper接口
 * @author TianTian
 * @date 2022/1/21 14:03
 */
@Mapper
public interface DamageListMapper extends BaseMapper<DamageList> {

    String  getNextDamageNumber();

    IPage<DamageList>  damageList(IPage<DamageList> page, @Param("damageListQuery") DamageListQuery damageListQuery);
}
