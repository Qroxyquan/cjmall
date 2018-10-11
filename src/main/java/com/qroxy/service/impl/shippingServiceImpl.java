package com.qroxy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.qroxy.common.ServerRespond;
import com.qroxy.dao.ShippingMapper;
import com.qroxy.pojo.Shipping;
import com.qroxy.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/11-4:43 PM
 */
@Service("iShippingService")
public class shippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerRespond add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.insert(shipping);
        if (count > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerRespond.createBySuccess("新增地址成功", result);

        }
        return ServerRespond.createByErrorMessage("新建地址失败");
    }

    /**
     * @desc:收获地址分页实现
     * @author:Qroxy
     * @date:2018/10/11 7:58 PM
     * @param:[userId, pageNum, pageSize]
     * @type:com.qroxy.common.ServerRespond<com.github.pagehelper.PageInfo>
     */
    @Override
    public ServerRespond<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerRespond.createBySuccess(pageInfo);

    }

    /**
     * @desc:搜索收获地址实现
     * @author:Qroxy
     * @date:2018/10/11 7:58 PM
     * @param:[userId, shippingId]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.pojo.Shipping>
     */

    @Override
    public ServerRespond<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServerRespond.createByErrorMessage("查询地址失败");

        } else {
            return ServerRespond.createBySuccess("查询地址成功", shipping);
        }
    }

    /**
     * @desc:更新收货地址实现
     * @author:Qroxy
     * @date:2018/10/11 7:59 PM
     * @param:[userId, shipping]
     * @type:com.qroxy.common.ServerRespond
     */
    @Override
    public ServerRespond update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
//        避免横向问题
        int count = shippingMapper.updateByUserId(shipping);
        if (count > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerRespond.createBySuccess("更新地址成功", result);

        }
        return ServerRespond.createByErrorMessage("更新地址失败");
    }

    /**
     * @desc:删除收货地址实现
     * @author:Qroxy
     * @date:2018/10/11 8:00 PM
     * @param:[userId, shippingId]
     * @type:com.qroxy.common.ServerRespond
     */
    @Override
    public ServerRespond delete(Integer userId, Integer shippingId) {

        int resultCount = shippingMapper.deleteByShippingIdAndUserId(userId, shippingId);
        if (resultCount > 0) {
            return ServerRespond.createBySuccess("删除地址成功");
        } else {
            return ServerRespond.createBySuccess("删除地址失败");
        }
    }

}
