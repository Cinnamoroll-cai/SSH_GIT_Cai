package com.edu.seiryo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.edu.seiryo.admin.dto.TreeDto;
import com.edu.seiryo.admin.pojo.GoodsType;
import com.edu.seiryo.admin.mapper.GoodsTypeMapper;
import com.edu.seiryo.admin.service.GoodsTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seiryo.admin.utils.AssertUtil;
import com.edu.seiryo.admin.utils.PageResultUtil;
import com.edu.seiryo.admin.utils.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品表类型实现类
 * @author TianTian
 * @date 2022/1/19 14:51
 */
@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {

	@Override
	public List<Map<String, Object>> queryAllGoodsType() {
		// TODO Auto-generated method stub
		// 1. 查询所有分类
        List<GoodsType> allTypes = this.list();

        // 2. 转换为 zTree 需要的扁平数据格式
        List<Map<String, Object>> result = new ArrayList<>();
        for (GoodsType type : allTypes) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", type.getId());
            // zTree 根节点的 pId 为 0
            // 需要将 -1 转为 0
            Integer parentId = type.getpId();
            if (parentId == -1) {
                parentId = 0;
            }
            node.put("pId", parentId);
            node.put("name", type.getName());
            result.add(node);
        }
        return result;
	}
	
	/**
     * 	递归获取子节点
     */
    private List<Map<String, Object>> getChildren(Integer parentId, List<GoodsType> allTypes) {
        List<Map<String, Object>> children = new ArrayList<>();
        for (GoodsType type : allTypes) {
            if (type.getpId() != null && type.getpId().equals(parentId)) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", type.getId());
                node.put("name", type.getName());
                node.put("children", getChildren(type.getId(), allTypes));
                children.add(node);
            }
        }
        return children;
    }

    /**
     * 	保存商品分类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void saveGoodsType(GoodsType goodsType) {
		// TODO Auto-generated method stub
    	// 参数校验
        AssertUtil.isTrue(StringUtil.isEmpty(goodsType.getName()), "类别名称不能为空");
        AssertUtil.isTrue(goodsType.getpId() == null, "父级类别不能为空");

        // 判断同级下是否已有同名分类
        QueryWrapper<GoodsType> wrapper = new QueryWrapper<>();
        wrapper.eq("name", goodsType.getName());
        wrapper.eq("p_id", goodsType.getpId());
        GoodsType existing = this.getOne(wrapper);
        AssertUtil.isTrue(existing != null, "该父级下已存在同名分类");

        // 设置节点状态：如果有子节点则为父节点(1)，否则为子节点(0)
        // 新增时默认为子节点(0)
        if (goodsType.getState() == null) {
            goodsType.setState(0);
        }
        // 默认图标
        if (StringUtil.isEmpty(goodsType.getIcon())) {
            goodsType.setIcon("icon-folder");
        }

        boolean saved = this.save(goodsType);
        AssertUtil.isTrue(!saved, "添加分类失败");

        // 如果新增的是子节点，需要将父节点状态改为 1（父节点）
        if (goodsType.getState() == 0) {
            GoodsType parent = this.getById(goodsType.getpId());
            if (parent != null && parent.getState() != 1) {
                parent.setState(1);
                this.updateById(parent);
            }
        }
	}

    /**
     * 	删除商品分类（检查是否有子节点）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void deleteGoodsType(Integer id) {
		// TODO Auto-generated method stub
    	AssertUtil.isTrue(id == null, "类别ID不能为空");

        // 检查是否有子节点
        QueryWrapper<GoodsType> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", id);
        long childCount = this.count(wrapper);
        AssertUtil.isTrue(childCount > 0, "该类别下存在子类别，不能删除");

        // 删除
        boolean removed = this.removeById(id);
        AssertUtil.isTrue(!removed, "删除失败");

        // 删除后，检查父节点是否还有其他子节点
        GoodsType deleted = this.getById(id);
        if (deleted != null && deleted.getpId() != null && deleted.getpId() != -1) {
            QueryWrapper<GoodsType> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("p_id", deleted.getpId());
            long remaining = this.count(checkWrapper);
            if (remaining == 0) {
                // 没有其他子节点了，将父节点状态改为 0（子节点）
                GoodsType parent = this.getById(deleted.getpId());
                if (parent != null && parent.getState() == 1) {
                    parent.setState(0);
                    this.updateById(parent);
                }
            }
        }
    }

}
