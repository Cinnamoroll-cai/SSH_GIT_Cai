package com.edu.seiryo.admin.config;



import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//表示将这个类作为配置类
//@MapperScan("cn.henu.mapper")：在启动时扫描Mapper接口，找到里面的内容
@MapperScan("com.edu.seiryo.admin.mapper")
public class MybatisPlusConfig {
    //乐观锁插件
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
    	PaginationInterceptor interceptor = new PaginationInterceptor();
        // 设置数据库类型为 Oracle
        interceptor.setDialectType("oracle");
        return interceptor;
    }

}
