package com.qroxy.service;

import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.Category;

import java.util.List;

/**
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/3-8:54 PM
 */
public interface ICategoryService {
    public ServerRespond addCategory(String categoryNAme, Integer parentId);
    public  ServerRespond updateCategoryName(Integer categoryId,String categoryName);
    public ServerRespond<List<Category>> getChildrenParallerCategory(Integer categoryId);
    public ServerRespond selectCategoryAndChilrenById(Integer categoryId);

}
