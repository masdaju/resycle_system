package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cg.entity.WasteClassify;
import com.cg.mapper.WasteClassifyMapper;
import com.cg.service.WasteClassifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 废品分类表 服务实现类
 * </p>
 *
 * @author 海カ布
 * @since 2024-12-26
 */
@Service
public class WasteClassifyServiceImpl extends ServiceImpl<WasteClassifyMapper, WasteClassify> implements WasteClassifyService {

    @Override
    public Page<WasteClassify> getClassify(String name, Integer current, Integer pageSize) {
        LambdaQueryWrapper<WasteClassify> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,WasteClassify::getName, name);
        Page<WasteClassify> aPage=new Page<>();
        if (current == null||pageSize == null) {
            aPage.setRecords(list(wrapper));
        }else {
            aPage = page(new Page<>(current, pageSize), wrapper);
        }
        return aPage;
    }
}
