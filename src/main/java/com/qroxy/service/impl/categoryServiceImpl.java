package com.qroxy.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qroxy.common.ServerRespond;
import com.qroxy.dao.CategoryMapper;
import com.qroxy.pojo.Category;
import com.qroxy.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/3-8:55 PM
 */
@Service("iCategoryService")
public class categoryServiceImpl implements ICategoryService {
    private Logger logger=LoggerFactory.getLogger(categoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;
/**
*@desc:更新分类
*
*@author:Qroxy
*
*@date:2018/10/3 9:47 PM
*
*@param:[categoryId, categoryName]
*
*@type:com.qroxy.common.ServerRespond
*
*/
    @Override
    public ServerRespond updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId==null||StringUtils.isBlank(categoryName)){
            return ServerRespond.createByErrorMessage("更新品类参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        category.setStatus(true);
        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount>0){
            return ServerRespond.createBySuccess("更新品类成功！");

        }
        return ServerRespond.createByErrorMessage("更新品类失败");
    }

/**
*@desc:添加分类实现
*
*@author:Qroxy
*
*@date:2018/10/3 9:47 PM
*
*@param:[categoryName, parentId]
*
*@type:com.qroxy.common.ServerRespond
*
*/
    @Override
    public ServerRespond addCategory(String categoryName,Integer parentId){
        if (parentId==null||StringUtils.isBlank(categoryName)){
            return ServerRespond.createByErrorMessage("添加品类参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount=categoryMapper.insertSelective(category);
        if (rowCount>0){
            return ServerRespond.createBySuccess("添加品类成功！");

        }
        return ServerRespond.createByErrorMessage("添加品类失败");
    }
    /**
    *@desc:根据分类id获取平级节点id
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 2:49 PM
    *
    *@param:[categoryId]
    *
    *@type:com.qroxy.common.ServerRespond<java.util.List<com.qroxy.pojo.Category>>
    *
    */
    @Override
    public ServerRespond<List<Category>> getChildrenParallerCategory(Integer categoryId){
        List<Category> categorieList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categorieList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerRespond.createBySuccess(categorieList);
    }
    /**
    *@desc:递归查询本节点的id及孩子节点id
    *
    *@author:Qroxy
    *
    *@date:2018/10/4 9:30 PM
    *
    *@param:[categoryId]
    *
    *@type:com.qroxy.common.ServerRespond
    *
    */
    @Override
    public ServerRespond<List<Integer>> selectCategoryAndChilrenById(Integer categoryId) {
        Set<Category> categorySet=Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryList=Lists.newArrayList();
        if (categoryId!=null){
            for (Category categoryItem:categorySet){
                categoryList.add(categoryItem.getId());
            }
        }
        return ServerRespond.createBySuccess(categoryList);
    }
    /**
    *@desc:递归查询
    *
    *@author:Qroxy
    *
    *@date:2018/10/4 9:30 PM
    *
    *@param:[categorySet, categoryId]
    *
    *@type:java.util.Set<com.qroxy.pojo.Category>
    *
    */
    //递归算法，算出子节点（重点理解）
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            categorySet.add(category);
        }
        //查找子节点，递归算法一定要有一个退出的条件
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem:categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
