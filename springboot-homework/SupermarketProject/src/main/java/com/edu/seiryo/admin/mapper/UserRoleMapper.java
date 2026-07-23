package com.edu.seiryo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seiryo.admin.pojo.UserRole;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    public List<String> findRoleByUserName(@Param("username") String username);
}
