package com.cg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.ChatMessage;
import com.cg.entity.view.VRelation;
import com.cg.mapper.ChatMessageMapper;
import com.cg.service.ChatMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {


    @Override
    public List<VRelation> getMyrelationship(Long loginIdAsLong) {
        return getBaseMapper().getMyrelationship(loginIdAsLong);
    }

    @Override
    public void addToMyrelationship(Long uid, Long friendId) {
        getBaseMapper().addToMyrelationship(uid, friendId);
    }

    @Override
    public List<ChatMessage> getchatlist(String acceptUserAccount, String sendUserAccount) {
        return baseMapper.getchatlist(acceptUserAccount, sendUserAccount);
    }

    @Override
    public void toggleLongTermContact(Long uid, Long fid, Integer status) {
        int a;
        switch (status) {
            case 0:
                a = 1;
                baseMapper.toggleLongTermContact(uid, fid, a);
                break;
            case 1:
                a=0;
                baseMapper.toggleLongTermContact(uid, fid, a);
                break;
            default:
                throw new RuntimeException("参数错误");
        }
    }
}
