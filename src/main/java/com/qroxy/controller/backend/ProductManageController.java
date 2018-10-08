package com.qroxy.controller.backend;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.qroxy.common.Const;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.Product;
import com.qroxy.pojo.User;
import com.qroxy.service.IFileSerivice;
import com.qroxy.service.IProductService;
import com.qroxy.service.IUserService;
import com.qroxy.service.impl.UserServiceImpl;
import com.qroxy.util.PropertiesUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/5-4:14 PM
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileSerivice iFileSerivice;

    /**
     *@desc:增加或更新产品接口
     *
     *@author:Qroxy
     *
     *@date:2018/10/5 4:37 PM
     *
     *@param:[session, product]
     *
     *@type:com.qroxy.common.ServerRespond
     *
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerRespond productSave(HttpSession session, Product product){


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage("用户未登录，请先登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }

    /**
     *@desc:更新产品销售转台接口
     *
     *@author:Qroxy
     *
     *@date:2018/10/5 4:51 PM
     *
     *@param:[session, productId, status]
     *
     *@type:com.qroxy.common.ServerRespond
     *
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerRespond setSaleStatus(HttpSession session, Integer productId,Integer status){


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //更新产品销售状态
            return iProductService.setSaleStatus(productId,status);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }

    /**
     *@desc:产品详情接口
     *
     *@author:Qroxy
     *
     *@date:2018/10/5 9:20 PM
     *
     *@param:[session, productId]
     *
     *@type:com.qroxy.common.ServerRespond
     *
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerRespond getdetail(HttpSession session, Integer productId){


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.manageProductDetail(productId);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }

    /**
     *@desc:获取商品列表接口（分页显示）
     *
     *@author:Qroxy
     *
     *@date:2018/10/5 9:58 PM
     *
     *@param:[session, pageSize, pageNum]
     *
     *@type:com.qroxy.common.ServerRespond
     *
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerRespond getList(HttpSession session, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.getProductList(pageNum,pageSize);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }

    /**
     * @desc:搜索产品接口
     * @author:Qroxy
     * @date:2018/10/7 3:48 PM
     * @param:[session, productName, productId, pageSize, pageNum]
     * @type:com.qroxy.common.ServerRespond
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerRespond searchList(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }

    /**
     * @desc:上传图片接口
     * @author:Qroxy
     * @date:2018/10/7 3:48 PM
     * @param:[file, request]
     * @type:com.qroxy.common.ServerRespond
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerRespond upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务


            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileSerivice.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            Map fileMap = new HashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerRespond.createBySuccess(fileMap);
        } else {
            return ServerRespond.createByErrorMessage("无权限操作！");
        }


    }

    /**
     * @desc:使用富文本上传图片并实时预览接口（直接使用mapput值，省去重新写一个返回对象的功夫）
     * @author:Qroxy
     * @date:2018/10/7 4:11 PM
     * @param:[session, file, request]
     * @type:java.util.Map
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");

            return resultMap;

        }
        //富文本对返回值有要求(simditor)
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
//            {
//                "success": true/false,
//                    "msg": "error message", # optional
//                "file_path": "[real file path]"
//            }

            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileSerivice.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");

                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
//            该插件要求返回
            response.addHeader("Accesss-Control-Allow-Headers", "X-File_Name");

            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");

            return resultMap;

        }


    }


}
