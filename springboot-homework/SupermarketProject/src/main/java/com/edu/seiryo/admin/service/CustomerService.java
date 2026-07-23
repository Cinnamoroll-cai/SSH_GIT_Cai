package com.edu.seiryo.admin.service;

import com.edu.seiryo.admin.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.query.CustomerQuery;

import java.util.Map;

/**
 * 客户表服务
 * @author TianTian
 * @date 2022/1/19 13:55
 */
public interface CustomerService extends IService<Customer> {

    Map<String, Object> customerList(CustomerQuery customerQuery);

    void saveCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(Integer[] ids);

    Customer findCustomerByName(String name);
}
