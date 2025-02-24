package com.cg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.view.VUser;
import com.cg.mapper.VUserMapper;
import com.cg.service.VUserService;
import org.springframework.stereotype.Service;

/**
* @author MIZUGI
* @description 针对表【v_user】的数据库操作Service实现
* @createDate 2024-10-13 20:33:04
*/
@Service
public class VUserServiceImpl extends ServiceImpl<VUserMapper, VUser>
    implements VUserService{

}




