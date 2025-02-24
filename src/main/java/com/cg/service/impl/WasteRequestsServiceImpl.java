package com.cg.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cg.entity.RequestWaste;
import com.cg.entity.RoleResources;
import com.cg.entity.WasteRequests;
import com.cg.mapper.WasteRequestsMapper;
import com.cg.service.RequestWasteService;
import com.cg.service.WasteRequestsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cg.utils.ListUtils.compare;

/**
 * <p>
 * 废品请求表 服务实现类
 * </p>
 *
 * @author 海カ布
 * @since 2024-12-27
 */
@Service
public class WasteRequestsServiceImpl extends ServiceImpl<WasteRequestsMapper, WasteRequests> implements WasteRequestsService {
@Resource
    private RequestWasteService requestWasteService;
    @Override
    @Transactional
    public boolean saveWasteRequests(Long requestId, List<Long> wid) {
        if (!wid.isEmpty()) {
            for (Long l : wid) {
                RequestWaste requestWaste = new RequestWaste();
                requestWaste.setRequestId(requestId);
                requestWaste.setWasteId(l);
                requestWasteService.save(requestWaste);
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateWasteRequests(Long requestId, List<Long> wid) {

        LambdaQueryWrapper<RequestWaste> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RequestWaste::getRequestId, requestId);
        List<RequestWaste> list = requestWasteService.list(wrapper);
        //得到修改前的废品id
        List<Long> resourceList =list.stream().map(RequestWaste::getWasteId).toList();
        //把新的废品id和修改前的废品id进行比较返回应该删除和新增的废品id
        List<List<Long>> resultLists = compare(resourceList, wid);
        System.out.println(resultLists.get(0));//删除的资源id
        System.out.println(resultLists.get(1));//新增的资源id
        //删除
        if (!resultLists.get(0).isEmpty()) {
            LambdaQueryWrapper<RequestWaste> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(RequestWaste::getRequestId, requestId);
            deleteWrapper.in(RequestWaste::getWasteId, resultLists.get(0));
            requestWasteService.remove(deleteWrapper);
        }
        //新增
        if (!resultLists.get(1).isEmpty()) {
            for (Long l : resultLists.get(1)) {
                RequestWaste requestWaste = new RequestWaste();
                requestWaste.setRequestId(requestId);
                requestWaste.setWasteId(l);
                requestWasteService.save(requestWaste);
            }
        }
        return true;
    }

    @Override
    public Page<WasteRequests> getPage(Integer current, Integer pageSize) {
        //获取当前登录用户id
        Long loginId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<WasteRequests> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WasteRequests::getUserId, loginId);
        Page<WasteRequests> apage =page(new Page<>(current, pageSize),queryWrapper);
        //获取每个请求的废品id
       apage.getRecords().forEach(item->{
           LambdaQueryWrapper<RequestWaste> wrapper = new LambdaQueryWrapper<>();
           wrapper.eq(RequestWaste::getRequestId,item.getRequestId());
           //把废品id拼接到请求中
            item.setWid(requestWasteService.list(wrapper).stream().map(RequestWaste::getWasteId).toList());
       });

        return apage;
    }

    @Override
    public Page<WasteRequests> getRequestByStatuspage(Integer current, Integer pageSize, Integer status) {
        LambdaQueryWrapper<WasteRequests> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(status!=null,WasteRequests::getStatus, status)
                .orderByAsc(WasteRequests::getAppointmentTime);
        return page(new Page<>(current, pageSize),queryWrapper);
    }


}
