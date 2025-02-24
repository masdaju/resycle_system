package com.cg.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cg.entity.Reports;
import com.cg.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 报告表 前端控制器
 * </p>
 *
 * @author 海カ布
 * @since 2024-12-26
 */
@RestController
@RequestMapping("/reports")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @GetMapping
    public SaResult list(@RequestParam(required = false) Integer current, @RequestParam(required = false) Integer pageSize) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Page<Reports> aPage = reportsService.page(new Page<>(current, pageSize));
        return SaResult.data(aPage);
    }

    @GetMapping(value = "/{id}")
    public SaResult getById(@PathVariable("id") String id) {
        Reports report = reportsService.getById(id);
        if (report!= null) {
            return SaResult.data(report);
        } else {
            return SaResult.error("未找到对应报告");
        }
    }

    @PostMapping(value = "/create")
    public SaResult create(@RequestBody Reports params) {
        params.setReportDate(new Date());
        try {
            reportsService.save(params);
            return SaResult.ok("创建成功");
        } catch (Exception e) {
            return SaResult.error("创建失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "deleteByIds")
    public SaResult getById(@RequestBody List<Long> ids) {
        System.out.println(ids);
        try {
            reportsService.removeBatchByIds(ids);
        } catch (Exception e) {
            SaResult.error("删除失败: " + e.getMessage());
        }
        return SaResult.ok("删除成功");
    }

    @PostMapping(value = "/update")
    public SaResult update(@RequestBody Reports params) {
        try {
            reportsService.updateById(params);
            return SaResult.ok("更新成功");
        } catch (Exception e) {
            return SaResult.error("更新失败：" + e.getMessage());
        }
    }
}