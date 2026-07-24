package com.edu.seiryo.admin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.mapper.GoodsUnitMapper;
import com.edu.seiryo.admin.pojo.GoodsUnit;
import com.edu.seiryo.admin.service.GoodsUnitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsUnitServiceImpl extends ServiceImpl<GoodsUnitMapper, GoodsUnit> implements GoodsUnitService {

	@Override
	public List<GoodsUnit> findAll() {
		// TODO Auto-generated method stub
		return this.list();
	}
}

