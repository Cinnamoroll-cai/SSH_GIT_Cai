package com.edu.seiryo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seiryo.admin.dto.TreeDto;
import com.edu.seiryo.admin.pojo.GoodsType;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
@Mapper
@Repository
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {
	// BaseMapper 已提供基本方法
}
