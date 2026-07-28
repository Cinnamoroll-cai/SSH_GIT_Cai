package com.edu.seiryo.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seiryo.admin.pojo.Customer;
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
	List<Customer> queryAllValidCustomer();
}
