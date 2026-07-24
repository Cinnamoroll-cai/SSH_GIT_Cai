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
     * 递归获取子节点
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

}
