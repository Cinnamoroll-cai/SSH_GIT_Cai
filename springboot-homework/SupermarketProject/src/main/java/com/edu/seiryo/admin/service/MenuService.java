package com.edu.seiryo.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seiryo.admin.dto.TreeDto;
import com.edu.seiryo.admin.pojo.Menu;
import com.edu.seiryo.admin.utils.PageResultUtil;
import io.swagger.models.auth.In;

import javax.lang.model.type.IntersectionType;
import java.util.List;
import java.util.Map;
/**
 * 菜单表服务类
 * @author TianTian
 * @date 2022/1/19 13:57
 */
public interface MenuService extends IService<Menu> {
	/**
     * 查询所有菜单（用于树形表格）
     * 前端 menu.js 请求 /menu/list
     */
    List<Menu> listAll();

    /**
     * 保存菜单（新增）
     * 前端 menu.add.js 提交 /menu/save
     */
    void saveMenu(Menu menu);

    /**
     * 更新菜单
     * 前端 menu.update.js 提交 /menu/update
     */
    void updateMenu(Menu menu);

    /**
     * 删除菜单（检查是否有子菜单）
     * 前端 menu.js 请求 /menu/delete
     */
    void deleteMenu(Integer id);
}
