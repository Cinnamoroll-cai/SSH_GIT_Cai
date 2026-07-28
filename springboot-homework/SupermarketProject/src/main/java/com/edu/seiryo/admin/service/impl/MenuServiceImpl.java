package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.dto.TreeDto;
import com.edu.seiryo.admin.mapper.MenuMapper;
import com.edu.seiryo.admin.pojo.Menu;
import com.edu.seiryo.admin.service.MenuService;
import com.edu.seiryo.admin.service.RoleMenuService;
import com.edu.seiryo.admin.utils.AssertUtil;
import com.edu.seiryo.admin.utils.PageResultUtil;
import com.edu.seiryo.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
	 /**
     * 查询所有菜单（按层级排序）
     * 前端 treetable 会根据 pId 自动构建树形结构
     */
	@Override
	public List<Menu> listAll() {
		// TODO Auto-generated method stub
		QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", 0);
        wrapper.orderByAsc("grade", "id");
        return this.list(wrapper);
	}
	
	 /**
     * 保存菜单（新增）
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMenu(Menu menu) {
		// TODO Auto-generated method stub
		// 1. 参数校验
        AssertUtil.isTrue(StringUtil.isEmpty(menu.getName()), "菜单名称不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(menu.getAclValue()), "权限码不能为空");
        AssertUtil.isTrue(menu.getGrade() == null, "菜单层级不能为空");
        AssertUtil.isTrue(menu.getpId() == null, "父级菜单不能为空");

        // 2. 检查同级下是否已存在同名菜单
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("name", menu.getName());
        wrapper.eq("p_id", menu.getpId());
        wrapper.eq("is_del", 0);
        Menu existing = this.getOne(wrapper);
        AssertUtil.isTrue(existing != null, "同级下已存在同名菜单");

        // 3. 设置默认值
        if (menu.getState() == null) {
            menu.setState(0);
        }
        if (menu.getIsDel() == null) {
            menu.setIsDel(0);
        }

        // 4. 如果是一级菜单（grade=0），设置默认图标
        if (menu.getGrade() == 0 && StringUtil.isEmpty(menu.getIcon())) {
            menu.setIcon("menu-icon");
        }

        // 5. 保存
        boolean saved = this.save(menu);
        AssertUtil.isTrue(!saved, "添加菜单失败");
	}
	
	/**
     * 更新菜单
     * 修改菜单层级时，子菜单层级需要同步调整
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMenu(Menu menu) {
		// TODO Auto-generated method stub
		 // 1. 参数校验
        AssertUtil.isTrue(menu.getId() == null, "菜单ID不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(menu.getName()), "菜单名称不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(menu.getAclValue()), "权限码不能为空");
        AssertUtil.isTrue(menu.getGrade() == null, "菜单层级不能为空");

        // 2. 获取原菜单信息
        Menu oldMenu = this.getById(menu.getId());
        AssertUtil.isTrue(oldMenu == null, "菜单不存在");

        // 3. 检查同级下是否已存在同名菜单（排除自身）
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("name", menu.getName());
        wrapper.eq("p_id", menu.getpId());
        wrapper.eq("is_del", 0);
        wrapper.ne("id", menu.getId());
        Menu existing = this.getOne(wrapper);
        AssertUtil.isTrue(existing != null, "同级下已存在同名菜单");

        // 4. 如果层级发生变化，需要同步更新子菜单的层级
        if (!oldMenu.getGrade().equals(menu.getGrade())) {
            // 计算层级变化量
            int gradeDiff = menu.getGrade() - oldMenu.getGrade();
            // 更新所有子菜单的层级
            updateChildrenGrade(menu.getId(), gradeDiff);
        }

        // 5. 执行更新
        boolean updated = this.updateById(menu);
        AssertUtil.isTrue(!updated, "更新菜单失败");
	}
	
	 /**
     * 递归更新子菜单的层级
     * @param parentId 父级菜单ID
     * @param gradeDiff 层级变化量（正数表示降级，负数表示升级）
     */
    private void updateChildrenGrade(Integer parentId, int gradeDiff) {
        // 查找所有子菜单
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", parentId);
        wrapper.eq("is_del", 0);
        List<Menu> children = this.list(wrapper);

        for (Menu child : children) {
            // 更新子菜单的层级
            child.setGrade(child.getGrade() + gradeDiff);
            // 限制层级范围：不能小于-1，不能大于2
            if (child.getGrade() < -1) {
                child.setGrade(-1);
            }
            if (child.getGrade() > 2) {
                child.setGrade(2);
            }
            this.updateById(child);
            // 递归更新孙菜单
            updateChildrenGrade(child.getId(), gradeDiff);
        }
    }

    /**
     * 删除菜单（检查是否有子菜单）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void deleteMenu(Integer id) {
		// TODO Auto-generated method stub
    	 // 1. 参数校验
        AssertUtil.isTrue(id == null, "菜单ID不能为空");

        // 2. 检查是否存在子菜单
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", id);
        wrapper.eq("is_del", 0);
        long childCount = this.count(wrapper);
        AssertUtil.isTrue(childCount > 0, "该菜单下存在子菜单，不能删除");

        // 3. 逻辑删除（将 is_del 设为 1）
        Menu menu = new Menu();
        menu.setId(id);
        menu.setIsDel(1);
        boolean deleted = this.updateById(menu);
        AssertUtil.isTrue(!deleted, "删除菜单失败");
		
	}

}

