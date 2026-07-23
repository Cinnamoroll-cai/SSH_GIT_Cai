package com.edu.seiryo.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.edu.seiryo.admin.pojo.CustomerReturnList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seiryo.admin.query.CustomerReturnListQuery;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户退货单表 Mapper 接口
 * </p>
 *
 * @author 老李
 */
@Mapper
public interface CustomerReturnListMapper extends BaseMapper<CustomerReturnList> {

    String  getNextCustomerReturnNumber();

    IPage<CustomerReturnList>  customerReturnList(IPage<CustomerReturnList> page, @Param("customerReturnListQuery") CustomerReturnListQuery customerReturnListQuery);
}
