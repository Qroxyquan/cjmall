package com.qroxy.service;

import com.github.pagehelper.PageInfo;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.Product;
import com.qroxy.vo.ProductDetaiVo;

/**
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/5-4:19 PM
 */
public interface IProductService {
    public ServerRespond saveOrUpdateProduct(Product product);

    ServerRespond<String> setSaleStatus(Integer productId, Integer status);

    public ServerRespond<ProductDetaiVo> manageProductDetail(Integer productId);

    public ServerRespond getProductList(int pageNum, int pageSize);

    public ServerRespond<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
}
