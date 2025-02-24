package com.cg.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cg.entity.ChatMessage;
import com.cg.entity.User;
import com.cg.entity.view.VRelation;
import com.cg.service.ChatMessageService;
import com.cg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private UserService userService;
    //获取联系人列表
    @GetMapping("/getMyRelationship")
    public SaResult getMyRelationship() {
        List<VRelation> relations = chatMessageService.getMyrelationship(StpUtil.getLoginIdAsLong());
    return SaResult.data(relations);
    }
    //添加联系人
    @PostMapping("/addToMyRelationship")
    public SaResult addToMyRelationship(@RequestParam String username) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount,username);
        Long uid = userService.getOne(queryWrapper).getId();
        try {
            chatMessageService.addToMyrelationship(StpUtil.getLoginIdAsLong(),uid);
        } catch (Exception e) {
            return SaResult.ok();
        }
        return SaResult.ok();
    }
    @GetMapping("/getChatMessage")
    public SaResult getChatMessage(@RequestParam String sendUserAccount, @RequestParam String acceptUserAccount)  {
        List<ChatMessage> chatMessages = chatMessageService.getchatlist(acceptUserAccount,sendUserAccount);
        return SaResult.data(chatMessages);
    }
    //发送消息
    @PostMapping(value = "/sendChatMessage")
    public SaResult create(@RequestBody ChatMessage params) {
        //设置发送时间
        params.setSendTime(new Date());
        System.out.println(params);
        chatMessageService.save(params);
        return SaResult.ok("created successfully");
    }
    @PostMapping("/toggleLongTermContact")
    public SaResult toggleLongTermContact(@RequestParam Long fid,@RequestParam Integer status) {
        chatMessageService.toggleLongTermContact(StpUtil.getLoginIdAsLong(),fid,status);
        return SaResult.ok();
    }
}
