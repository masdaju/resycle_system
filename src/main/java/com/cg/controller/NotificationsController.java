package com.cg.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cg.entity.Notifications;
import com.cg.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 消息通知表 前端控制器
 * </p>
 *
 * @author 海カ布
 * @since 2024-12-26
 */
@RestController
@RequestMapping("/notifications")
public class NotificationsController {

    @Autowired
    private NotificationsService notificationsService;

    @GetMapping("/MyMsg")
    public SaResult list(@RequestParam(required = false) Integer current
            , @RequestParam(required = false) Integer pageSize,
                         @RequestParam(required = false) String startTime,
                         @RequestParam(required = false) String endTime) {
        if (current == null || pageSize == null) {
            current = 1;
            pageSize = 10;
        }
        LambdaQueryWrapper<Notifications> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notifications::getUserId, StpUtil.getLoginIdAsLong())
                .between(startTime != null&&endTime!=null, Notifications::getSentAt, startTime, endTime)
                .orderByDesc(Notifications::getSentAt);
        Page<Notifications> aPage = notificationsService.page(new Page<>(current, pageSize),queryWrapper);
        return SaResult.data(aPage);
    }

    @GetMapping(value = "/{id}")
    public SaResult getById(@PathVariable("id") String id) {
        Notifications notification = notificationsService.getById(id);
        if (notification!= null) {
            return SaResult.data(notification);
        } else {
            return SaResult.error("未找到指定消息通知");
        }
    }

    @PostMapping(value = "/setNotification")
    public SaResult create(@RequestParam String message, @RequestParam Long userId) {
        try {
            Notifications params = new Notifications();
            params.setMessage(message);
            params.setUserId(userId);
            params.setSentAt(new Date());
            notificationsService.save(params);
            return SaResult.ok("消息通知创建成功");
        } catch (Exception e) {
            return SaResult.error("消息通知创建失败: " + e.getMessage());
        }
    }

    @PostMapping(value = "deleteByIds")
    public SaResult getById(@RequestBody List<Long> ids) {
        System.out.println(ids);
        try {
            notificationsService.removeBatchByIds(ids);
        } catch (Exception e) {
            SaResult.error("删除失败: " + e.getMessage());
        }
        return SaResult.ok("删除成功");
    }
    @PostMapping(value = "/update")
    public SaResult update(@RequestBody Notifications params) {
        try {
            notificationsService.updateById(params);
            return SaResult.ok("消息通知更新成功");
        } catch (Exception e) {
            return SaResult.error("消息通知更新失败: " + e.getMessage());
        }
    }
}