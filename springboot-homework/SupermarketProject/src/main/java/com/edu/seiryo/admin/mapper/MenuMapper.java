package com.edu.seiryo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seiryo.admin.dto.TreeDto;
import com.edu.seiryo.admin.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {
	// BaseMapper 已提供增删改查方法
}
