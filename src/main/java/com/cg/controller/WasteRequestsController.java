package com.cg.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cg.entity.RequestWaste;
import com.cg.entity.WasteRequests;
import com.cg.service.RequestWasteService;
import com.cg.service.WasteRequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 废品请求表 前端控制器
 * </p>
 *
 * @author 海カ布
 * @since 2024-12-27
 */
@RestController
@RequestMapping("/waste-requests")
public class WasteRequestsController {

    @Autowired
    private WasteRequestsService wasteRequestsService;
    @Autowired
    private RequestWasteService requestWasteService;

    @GetMapping(value = "/getMyRequest")
    public SaResult list(@RequestParam(required = false) Integer current
            , @RequestParam(required = false) Integer pageSize
             ) {
        if (current == null||pageSize == null) {
            current = 1;
            pageSize = 10;
        }
      Page<WasteRequests> aPage = wasteRequestsService.getPage(current,pageSize);

            return SaResult.data(aPage);
    }
    @GetMapping("getRequestByStatus")
    public SaResult getRequestByStatus(@RequestParam(required = false) Integer current
                                       , @RequestParam(required = false) Integer pageSize
                                       , @RequestParam(required = false) Integer status) {
        if (current == null||pageSize == null) {
            current = 1;
            pageSize = 10;
        }
        Page<WasteRequests> aPage = wasteRequestsService.getRequestByStatuspage(current, pageSize,status);
        return SaResult.data(aPage);
    }


    @PostMapping(value = "deleteByIds")
    public SaResult getById(@RequestBody List<Long> ids) {
        try {
            requestWasteService.remove(new LambdaQueryWrapper<RequestWaste>().in(RequestWaste::getRequestId, ids));
            wasteRequestsService.removeBatchByIds(ids);
        } catch (Exception e) {
            SaResult.error("删除失败: " + e.getMessage());
        }
        return SaResult.ok("删除成功");
    }
//添加回收请求+添加废品和请求的关联
    @PostMapping(value = "/create")
    @Transactional
    public SaResult create(@RequestBody WasteRequests params) {
        try {
            wasteRequestsService.save(params.setUserId(StpUtil.getLoginIdAsLong()));
            if (wasteRequestsService.saveWasteRequests(params.getRequestId(),params.getWid())) {
                return SaResult.ok("废品请求创建成功");
            }
            return SaResult.ok("废品请求创建成功");
        } catch (Exception e) {
            return SaResult.error("废品请求创建失败: " + e.getMessage());
        }
    }


    @PostMapping(value = "/update")
    public SaResult update(@RequestBody WasteRequests params) {
        try {
            wasteRequestsService.updateById(params);
            if (wasteRequestsService.updateWasteRequests(params.getRequestId(),params.getWid())) {
                return SaResult.ok("废品请求更新成功");
            }
            return SaResult.ok("废品请求更新成功");
        } catch (Exception e) {
            return SaResult.error("废品请求更新失败: " + e.getMessage());
        }
    }
}