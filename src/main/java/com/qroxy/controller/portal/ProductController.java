package com.qroxy.controller.portal;

import com.github.pagehelper.PageInfo;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.Product;
import com.qroxy.service.IProductService;
import com.qroxy.vo.ProductDetaiVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/7-7:07 PM
 */
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    /**
     * @desc:前台商品详情接口
     * @author:Qroxy
     * @date:2018/10/7 7:30 PM
     * @param:[productId]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.ProductDetaiVo>
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerRespond<ProductDetaiVo> detail(Integer productId) {

        return iProductService.getProductDetail(productId);

    }

    /**
     * @desc:前台搜索接口
     * @author:Qroxy
     * @date:2018/10/7 7:30 PM
     * @param:[keyword, categoryId, pageNum, pageSize]
     * @type:com.qroxy.common.ServerRespond<com.github.pagehelper.PageInfo>
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerRespond<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageNum", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "orderBy", defaultValue = "10") String orderBy) {
        return iProductService.getProductByKeywordOrCategoryId(keyword, categoryId, pageNum, pageSize, orderBy);


    }
}
