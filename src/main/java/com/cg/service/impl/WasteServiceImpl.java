package com.cg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cg.entity.SysFile;
import com.cg.entity.Waste;
import com.cg.mapper.WasteMapper;
import com.cg.service.SysFileService;
import com.cg.service.WasteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * <p>
 * 废品表 服务实现类
 * </p>
 *
 * @author 海カ布
 * @since 2024-12-26
 */
@Service
@RequiredArgsConstructor
public class WasteServiceImpl extends ServiceImpl<WasteMapper, Waste> implements WasteService {
    private final SysFileService sysFileService;
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${preview.url}")
    private String previewUrl;
    @Override
    @Transactional
    public boolean saveWaste(Waste waste, MultipartFile file) {
        String filename = sysFileService.upload(file);
        waste.setImgUrl(previewUrl+filename);
        waste.setCreateDate(new Date());
        System.out.println(waste);
        save(waste);
        return true;
    }

    @Override
    @Transactional
    public void updateWaste(Waste waste, MultipartFile file) {
        String imgUrl = waste.getImgUrl();
        String realPath = uploadPath+ StringUtils.topath(imgUrl);
        sysFileService.delFromDisk(realPath);
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysFile::getFileUrl,imgUrl);
        //删除数据库记录
        sysFileService.remove(wrapper);
        String filename = sysFileService.upload(file);
        waste.setImgUrl(previewUrl+filename);
        updateById(waste);

    }
}
