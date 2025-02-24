package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.Role;
import com.cg.entity.RoleResources;
import com.cg.mapper.RoleMapper;
import com.cg.service.RoleResourcesService;
import com.cg.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author MIZUGI
* @description 针对表【sys_role(角色表)】的数据库操作Service实现
* @createDate 2024-10-10 10:27:35
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{
    @Autowired
    private RoleResourcesService roleResourcesService;
    @Override
    public Page<Role> getPage(Page<Role> page, LambdaQueryWrapper<Role> wrapper) {
        Page<Role> aPage = page(page,wrapper);
        List<RoleResources> roleResourcesList = roleResourcesService.list();
        // 根据 roleId 分组并收集 resources_id
        Map<Long, List<Long>> roleIdToResIdsMap = roleResourcesList.stream()
                .collect(Collectors.groupingBy(
                        RoleResources::getRoleId,
                        Collectors.mapping(RoleResources::getResourcesId, Collectors.toList())
                ));
        List<Role> roles = aPage.getRecords();
        roles.forEach(role ->
                role.setResId(roleIdToResIdsMap.getOrDefault(role.getId(), new ArrayList<>()))
        );
        return aPage;
    }
}




