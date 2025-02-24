package com.cg.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cg.entity.Role;
import com.cg.entity.RoleResources;
import com.cg.service.RoleResourcesService;
import com.cg.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-10-13
 */
@RestController

@RequestMapping("/sys-role")
public class RoleController {


    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourcesService roleResourcesService;

    @GetMapping
    public SaResult list(@RequestParam(required = false) Integer current,
                         @RequestParam(required = false) Integer pageSize,
                         @RequestParam(required = false) String name) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (name == null) {
            wrapper=null;
        }else {
            wrapper.like(Role::getName, name);
        }
        Page<Role> page;
        if (current == null || pageSize == null) {
             page = new Page<>();
        } else {
             page = new Page<>(current, pageSize);
        }
        Page<Role> aPage =roleService.getPage(page, wrapper);
        return SaResult.data(aPage);
    }

    @GetMapping(value = "/{id}")
    public SaResult getById(@PathVariable("id") String id) {
        return SaResult.data(roleService.getById(id));
    }
    @PostMapping(value = "/create")
    public SaResult create(@RequestBody Role params) {
        roleService.save(params);
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getName,params.getName());
        Role one = roleService.getOne(lambdaQueryWrapper);
        if (roleResourcesService.refresh(one.getId(), params.getResId())) {
            return SaResult.error("update failed");
        }
        return SaResult.ok("created successfully");
    }
    @PostMapping(value = "/delete/{id}")
    public SaResult delete(@PathVariable("id") String id) {
        LambdaQueryWrapper<RoleResources> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(RoleResources::getRoleId,id);
        roleResourcesService.remove(wrapper);
        roleService.removeById(id);
        return SaResult.ok("deleted successfully");
    }
    @PostMapping(value = "/update")
    public SaResult update(@RequestBody Role params) {
        if (!roleResourcesService.refresh(params.getId(), params.getResId())) {
            return SaResult.error("update failed");
        }
        roleService.updateById(params);
        return SaResult.ok("updated successfully");
    }
}
