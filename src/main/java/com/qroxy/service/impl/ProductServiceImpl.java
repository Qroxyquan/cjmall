package com.qroxy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;
import com.qroxy.dao.CategoryMapper;
import com.qroxy.dao.ProductMapper;
import com.qroxy.pojo.Category;
import com.qroxy.pojo.Product;
import com.qroxy.service.IProductService;
import com.qroxy.util.DateTimeUtil;
import com.qroxy.util.PropertiesUtil;
import com.qroxy.vo.ProductDetaiVo;
import com.qroxy.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/5-4:19 PM/*-
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * @desc:增加或更新产品实现
     * @author:Qroxy
     * @date:2018/10/5 4:38 PM
     * @param:[product]
     * @type:com.qroxy.common.ServerRespond
     */
    @Override
    public ServerRespond saveOrUpdateProduct(Product product) {

        if (product != null) {
            //如果子图不为空，则分割子图取第一张图为主图
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }

            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerRespond.createBySuccess("更新成功");
                } else {
                    return ServerRespond.createByErrorMessage("更新失败");

                }

            } else {


                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerRespond.createBySuccess("新增产品成功");
                } else {
                    return ServerRespond.createByErrorMessage("新增产品失败");
                }

            }

        }
        return ServerRespond.createByErrorMessage("更新或新增的产品参数不正确！");
    }


    /**
    *@desc:修改产品销售状态实现
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 4:49 PM
    *
    *@param:[productId, status]
    *
    *@type:com.qroxy.common.ServerRespond<java.lang.String>
    *
    */
    @Override
    public ServerRespond<String> setSaleStatus(Integer productId,Integer status){
        if (productId==null||status==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount=productMapper.updateByPrimaryKeySelective(product);
        if (rowCount>0){
            return ServerRespond.createBySuccessMessage("修改产品销售状态成功");
        }else {
            return  ServerRespond.createByErrorMessage("修改产品销售状态失败");
        }
    }
    /**
    *@desc:产品详情实现
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 9:21 PM
    *
    *@param:[productId]
    *
    *@type:com.qroxy.common.ServerRespond<com.qroxy.vo.ProductDetaiVo>
    *
    */
    @Override
    public ServerRespond<ProductDetaiVo> manageProductDetail(Integer productId){
         if (productId==null){
             ServerRespond.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());

         }
         Product product=productMapper.selectByPrimaryKey(productId);
         if (product==null){
             return  ServerRespond.createByErrorMessage("产品已经下架或已经删除");

         }
         //VO对象--value object
        //pojo->(bussiness object)->vo(view object)
        ProductDetaiVo productDetaiVo=assembleProductDetailVo(product);

        return  ServerRespond.createBySuccess(productDetaiVo);


    }
    /**
    *@desc:获取商品列表实现
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 9:57 PM
    *
    *@param:[pageNum, pageSize]
    *
    *@type:com.qroxy.common.ServerRespond
    *
    */
    @Override
    public  ServerRespond getProductList(int pageNum,int pageSize){
        //startpage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList=productMapper.selectList();

        List<ProductListVo> productListVoList=Lists.newArrayList();

        for(Product productItem:productList){
            ProductListVo productListVo=assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult=new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerRespond.createBySuccess(pageResult);
    }
    /**
    *@desc:Vo包装返回类
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 9:21 PM
    *
    *@param:[product]
    *
    *@type:com.qroxy.vo.ProductDetaiVo
    *
    */
    private ProductDetaiVo assembleProductDetailVo(Product product) {
        ProductDetaiVo productDetaiVo = new ProductDetaiVo();
        productDetaiVo.setId(product.getId());
        productDetaiVo.setSubtitle(product.getSubtitle());
        productDetaiVo.setPrice(product.getPrice());
        productDetaiVo.setMainImage(product.getMainImage());
        productDetaiVo.setCategoryId(product.getCategoryId());
        productDetaiVo.setStatus(product.getStatus());
        productDetaiVo.setStock(product.getStock());
        productDetaiVo.setDetail(product.getDetail());
        productDetaiVo.setName(product.getName());
        //imageHost
        productDetaiVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.sell.com/"));
        //parentCategoryId
        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category==null){
            //默认根节点
            productDetaiVo.setParentCategoryId(0);

        }else {
            productDetaiVo.setParentCategoryId(category.getParentId());
        }
        //creatTime
        productDetaiVo.setCreateTime(DateTimeUtil.dateToString(product.getCreateTime()));
        //updateTime
        productDetaiVo.setUpdateTime(DateTimeUtil.dateToString(product.getUpdateTime()));
    return productDetaiVo;

    }
    /**
    *@desc:组装对象
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 9:45 PM
    *
    *@param:[product]
    *
    *@type:com.qroxy.vo.ProductListVo
    *
    */
    private ProductListVo assembleProductListVo(Product product){

        ProductListVo productListVo=new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.sell.com/"));
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStock(product.getStock());
        return  productListVo;

    }
    /**
    *@desc:搜索产品列表实现
    *
    *@author:Qroxy
    *
    *@date:2018/10/6 12:57 AM
    *
    *@param:[productName, productId, pageNum, pageSize]
    *
    *@type:com.qroxy.common.ServerRespond<com.github.pagehelper.PageInfo>
    *
    */
    @Override
    public ServerRespond<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){

        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName=new  StringBuilder().append("%").append(productName).append("%").toString();

        }
        List<Product> productList=productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList=Lists.newArrayList();
        for(Product productItem:productList){
            ProductListVo productListVo=assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult=new PageInfo(productList);
        pageResult.setList(productListVoList);
        return  ServerRespond.createBySuccess(pageResult);
    }

}
