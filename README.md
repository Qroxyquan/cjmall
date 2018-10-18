#### 本次学习收获
整合SSM框架（Spring+SpringMVC+Mybatis），采用前后端分离的架构方式开发的仿天猫在线电商平台项目。通过完成本项目，掌握了如下技能：

- *首先，当然是java web开发，包括javaEE的核心技术的实际应用；*
- *其次，学习目前最流行的javaweb框架——SSM框架的整合，理解其；*
- *再者，前后端分离式设计的具体实现。*
- *最后，如果有时间将采用RESTfulAPI方式演进项目*

##### 开发本项目用到的一些工具：

- IDE我使用IDEA（项目由Maven构建，IDE选择不影响）
- JDK版本为1.8 64位
- Maven版本是3.0.5
- mysql-server-8.0
- nginx/1.11.7
- pageHelper：一个非常好用的分页工具，实现原理Aop技术
- mybatisCodeHelper：mybatis插件，有免费版本！神器！！
- ngrok：内网穿透工具，支付宝对接设置时需要用到
- FileZilla：ftp/sftp文件管理工具

接口展示：
------

####1.统计用户、商品、订单数量


**/manage/statistic/base_count.do**

> request

```
无

```

> response

success

```

{
    "status": 0,
    "data": {
        userCount: 123,
        productCount: 456,
        orderCount: 789
    }
}

```


fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
或

{
    "status": 1,
    "msg": "出错啦"
}
```

####1.后台管理员登录

**/manage/user/login.do**


> request

```
String username,
String password
```

> response

success

```
{
    "status": 0,
    "data": {
        "id": 12,
        "username": "aaa",
        "email": "aaa@163.com",
        "phone": null,
        "role": 0,
        "createTime": 1479048325000,
        "updateTime": 1479048325000
    }
}
```

fail
```
{
    "status": 1,
    "msg": "密码错误"
}
```

------


####2.用户列表

**/manage/user/list.do**


> request

```
pageSize(default=10)
pageNum(default=1)
```

> response

success

```
{
    "status": 0,
    "data": {
        "pageNum": 1,
        "pageSize": 3,
        "size": 3,
        "orderBy": null,
        "startRow": 1,
        "endRow": 3,
        "total": 16,
        "pages": 6,
        "list": [
            {
                "id":17,
                "username":"rosen",
                "password":"",
                "email":"rosen1@happymmall.com",
                "phone":"15011111111",
                "question":"啊哈哈",
                "answer":"服不服",
                "role":0,
                "createTime":1489719093000,
                "updateTime":1513682138000
            },
            {
                "id":17,
                "username":"rosen",
                "password":"",
                "email":"rosen1@happymmall.com",
                "phone":"15011111111",
                "question":"啊哈哈",
                "answer":"服不服",
                "role":0,
                "createTime":1489719093000,
                "updateTime":1513682138000
            }
        ],
        "firstPage": 1,
        "prePage": 0,
        "nextPage": 2,
        "lastPage": 6,
        "isFirstPage": true,
        "isLastPage": false,
        "hasPreviousPage": false,
        "hasNextPage": true,
        "navigatePages": 8,
        "navigatepageNums": [
          1,
          2,
          3,
          4,
          5,
          6
        ]
    }
}
```

fail
```
{
  "status": 10,
  "msg": "用户未登录,请登录"
}


或

{
  "status": 1,
  "msg": "没有权限"
}



```
------

####3.模板

**/REPLACE/.do**

> request

```
k
```

> response

success

```
k
```

fail
```
k
```

------
[MENU]

------

####1.产品list

http://localhost:8080/manage/product/list.do

**/manage/product/list.do**

> request

```
pageNum(default=1)
pageSize(default=10)

```

> response

success

```
{
    "status": 0,
    "data": {
        "pageNum": 1,
        "pageSize": 10,
        "size": 2,
        "orderBy": null,
        "startRow": 1,
        "endRow": 2,
        "total": 2,
        "pages": 1,
        "list": [
            {
                "id": 1,
                "categoryId": 3,
                "name": "iphone7",
                "subtitle": "双十一促销",
                "mainImage": "mainimage.jpg",
                "status":1,
                "price": 7199.22
            },
            {
                "id": 2,
                "categoryId": 2,
                "name": "oppo R8",
                "subtitle": "oppo促销进行中",
                "mainImage": "mainimage.jpg",
                "status":1,
                "price": 2999.11
            }
        ],
        "firstPage": 1,
        "prePage": 0,
        "nextPage": 0,
        "lastPage": 1,
        "isFirstPage": true,
        "isLastPage": true,
        "hasPreviousPage": false,
        "hasNextPage": false,
        "navigatePages": 8,
        "navigatepageNums": [
            1
        ]
    }
}

```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}

```

------

####2.产品搜索

http://localhost:8080/manage/product/search.do?productName=p

http://localhost:8080/manage/product/search.do?productId=1



**/manage/product/search.do**

> request

```
productName
productId
pageNum(default=1)
pageSize(default=10)


```

> response

success

```
{
    "status": 0,
    "data": {
        "pageNum": 1,
        "pageSize": 10,
        "size": 1,
        "orderBy": null,
        "startRow": 1,
        "endRow": 1,
        "total": 1,
        "pages": 1,
        "list": [
            {
                "id": 1,
                "categoryId": 3,
                "name": "iphone7",
                "subtitle": "双十一促销",
                "mainImage": "mainimage.jpg",
                "price": 7199.22
            }
        ],
        "firstPage": 1,
        "prePage": 0,
        "nextPage": 0,
        "lastPage": 1,
        "isFirstPage": true,
        "isLastPage": true,
        "hasPreviousPage": false,
        "hasNextPage": false,
        "navigatePages": 8,
        "navigatepageNums": [
            1
        ]
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}

```

------

####3.图片上传

**/manage/product/upload.do**

> request

```

<form name="form2" action="/manage/product/upload.do" method="post"  enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="upload"/>
</form>

```

> response

success

```
{
    "status": 0,
    "data": {
        "uri": "e6604558-c0ff-41b9-b6e1-30787a1e3412.jpg",
        "url": "http://img.happymmall.com/e6604558-c0ff-41b9-b6e1-30787a1e3412.jpg"
    }
}

```

fail
```
status!=0的时候
```

------

####4.产品详情

http://localhost:8080/manage/product/detail.do?productId=2

**/manage/product/detail.do**

> request

```
productId
```

> response

success

```
{
    "status": 0,
    "data": {
        "id": 2,
        "categoryId": 2,
        "parentCategoryId":1,
        "name": "oppo R8",
        "subtitle": "oppo促销进行中",
        "imageHost": "http://img.happymmall.com/",
        "mainImage": "mainimage.jpg",
        "subImages": "[\"mmall/aa.jpg\",\"mmall/bb.jpg\",\"mmall/cc.jpg\",\"mmall/dd.jpg\",\"mmall/ee.jpg\"]",
        "detail": "richtext",
        "price": 2999.11,
        "stock": 71,
        "status": 1,
        "createTime": "2016-11-20 14:21:53",
        "updateTime": "2016-11-20 14:21:53"
    }
}

```

fail
```
{
    "status": 1,
    "msg": "没有权限"
}
```

------

####5.产品上下架

http://localhost:8080/manage/product/set_sale_status.do?productId=1&status=1

**/manage/product/set_sale_status.do**

> request

```
productId
status
```

> response

success

```
{
    "status": 0,
    "data": "修改产品状态成功"
}
```

fail
```
{
    "status": 1,
    "data": "修改产品状态失败"
}
```

------

####6.新增OR更新产品

新增

新增
http://localhost:8080/manage/product/save.do?categoryId=1&name=三星洗衣机&subtitle=三星大促销&subImages=test.jpg,11.jpg,2.jpg,3.jpg&detail=detailtext&price=1000&stock=100&status=1


更新
http://localhost:8080/manage/product/save.do?categoryId=1&name=三星洗衣机&subtitle=三星大促销&subImages=test.jpg&detail=detailtext&price=1000&stock=100&status=1&id=3


**/manage/product/save.do**

> request

```
categoryId=1&name=三星洗衣机&subtitle=三星大促销&mainImage=sss.jpg&subImages=test.jpg&detail=detailtext&price=1000&stock=100&status=1&id=3
```

> response

success

```
{
    "status": 0,
    "data": "更新产品成功"
}

或


{
    "status": 0,
    "data": "新增产品成功"
}

```

fail
```
{
    "status": 1,
    "data": "更新产品失败"
}
```

------



####7.富文本上传图片


**/manage/product/richtext_img_upload.do**

> request

```
<form name="form2" action="/manage/product/upload.do" method="post"  enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="upload"/>
</form>

```

> response

success

```
{
    "file_path": "http://img.happymmall.com/5fb239f2-0007-40c1-b8e6-0dc11b22779c.jpg",
    "msg": "上传成功",
    "success": true
}

```

fail
```
{
    "success": false,
    "msg": "error message",
    "file_path": "[real file path]"
}
```

------
[MENU]

------

####1.获取品类子节点(平级)

http://localhost:8080/manage/category/get_category.do
http://localhost:8080/manage/category/get_category.do?categoryId=0


http://localhost:8080/manage/category/get_category.do?categoryId=2

**/manage/category/get_category.do**

> request

```
categoryId(default=0)

```

> response

success

```

{
    "status": 0,
    "data": [
        {
            "id": 2,
            "parentId": 1,
            "name": "手机",
            "status": true,
            "sortOrder": 3,
            "createTime": 1479622913000,
            "updateTime": 1479622913000
        },
        {
            "id": 4,
            "parentId": 1,
            "name": "移动座机",
            "status": true,
            "sortOrder": 5,
            "createTime": 1480059936000,
            "updateTime": 1480491941000
        }
    ]
}

```


http://localhost:8080/manage/category/get_category.do?categoryId=19


fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
或

{
    "status": 1,
    "msg": "未找到该品类"
}
```

------

####2.增加节点

**/manage/category/add_category.do**

> request

```
parentId(default=0)
categoryName
```

> response

success

```
{
    "status": 0,
    "msg": "添加品类成功"
}
```

fail
```
{
    "status":1,
    "msg": "添加品类失败"
}
```

------

####3.修改品类名字

http://localhost:8080/manage/category/set_category_name.do?categoryId=999&categoryName=%E5%98%BB%E5%98%BB
http://localhost:8080/manage/category/set_category_name.do?categoryId=1&categoryName=%E5%98%BB%E5%98%BB


**/manage/category/set_category_name.do**

> request

```
categoryId
categoryName
```

> response

success

```
{
    "status": 0,
    "msg": "更新品类名字成功"
}
```

fail
```
{
    "status": 1,
    "msg": "更新品类名字失败"
}
```

------


####4.获取当前分类id及递归子节点categoryId

http://localhost:8080/manage/category/get_deep_category.do?categoryId=100001


**/manage/category/get_deep_category.do**

> request

```
categoryId
```

> response

success

```
{
    "status": 0,
    "data": [
        100009,
        100010,
        100001,
        100006,
        100007,
        100008
    ]
}
```

fail
```
{
    "status": 1,
    "msg": "无权限"
}
```

------
[MENU]

------

####1.订单List

http://localhost:8080/manage/order/list.do?pageSize=3

**/manage/order/list.do**

> request

```
pageSize(default=10)
pageNum(default=1)
```

> response

success

```
{
  "status": 0,
  "data": {
    "pageNum": 1,
    "pageSize": 3,
    "size": 3,
    "orderBy": null,
    "startRow": 1,
    "endRow": 3,
    "total": 16,
    "pages": 6,
    "list": [
      {
        "orderNo": 1485158676346,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:36",
        "orderItemVoList": [
          {
            "orderNo": 1485158676346,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:36"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "shippingVo": null
      },
      {
        "orderNo": 1485158675516,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:35",
        "orderItemVoList": [
          {
            "orderNo": 1485158675516,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:35"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "receiverName": "geely",
        "shippingVo": null
      },
      {
        "orderNo": 1485158675316,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:35",
        "orderItemVoList": [
          {
            "orderNo": 1485158675316,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:35"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "receiverName": "geely",
        "shippingVo": null
      }
    ],
    "firstPage": 1,
    "prePage": 0,
    "nextPage": 2,
    "lastPage": 6,
    "isFirstPage": true,
    "isLastPage": false,
    "hasPreviousPage": false,
    "hasNextPage": true,
    "navigatePages": 8,
    "navigatepageNums": [
      1,
      2,
      3,
      4,
      5,
      6
    ]
  }
}
```

fail
```
{
  "status": 10,
  "msg": "用户未登录,请登录"
}


或

{
  "status": 1,
  "msg": "没有权限"
}



```

------

####2.按订单号查询

http://localhost:8080/manage/order/search.do?orderNo=1480515829406

**/manage/order/search.do**

> request

```
orderNo
```

> response

success

```
{
  "status": 0,
  "data": {
    "pageNum": 1,
    "pageSize": 3,
    "size": 3,
    "orderBy": null,
    "startRow": 1,
    "endRow": 3,
    "total": 16,
    "pages": 6,
    "list": [
      {
        "orderNo": 1485158676346,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:36",
        "orderItemVoList": [
          {
            "orderNo": 1485158676346,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:36"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "shippingVo": null
      },
      {
        "orderNo": 1485158675516,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:35",
        "orderItemVoList": [
          {
            "orderNo": 1485158675516,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:35"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "receiverName": "geely",
        "shippingVo": null
      },
      {
        "orderNo": 1485158675316,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:35",
        "orderItemVoList": [
          {
            "orderNo": 1485158675316,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:35"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "receiverName": "geely",
        "shippingVo": null
      }
    ],
    "firstPage": 1,
    "prePage": 0,
    "nextPage": 2,
    "lastPage": 6,
    "isFirstPage": true,
    "isLastPage": false,
    "hasPreviousPage": false,
    "hasNextPage": true,
    "navigatePages": 8,
    "navigatepageNums": [
      1,
      2,
      3,
      4,
      5,
      6
    ]
  }
}
```

fail
```
{
  "status": 1,
  "msg": "没有找到订单"
}
```

------

####3.订单详情

http://localhost:8080/manage/order/detail.do?orderNo=1480515829406

**/manage/order/detail.do**

> request

```
orderNo
```

> response

success

```
{
  "status": 0,
  "data": {
    "orderNo": 1480515829406,
    "payment": 30000.00,
    "paymentType": 1,
    "paymentTypeDesc": "在线支付",
    "postage": 0,
    "status": 10,
    "statusDesc": "未支付",
    "paymentTime": "",
    "sendTime": "",
    "endTime": "",
    "closeTime": "",
    "createTime": "2016-11-30 22:23:49",
    "orderItemVoList": [
      {
        "orderNo": 1480515829406,
        "productId": 1,
        "productName": "iphone7",
        "productImage": "mainimage.jpg",
        "currentUnitPrice": 10000.00,
        "quantity": 1,
        "totalPrice": 10000.00,
        "createTime": "2016-11-30 22:23:49"
      },
      {
        "orderNo": 1480515829406,
        "productId": 2,
        "productName": "oppo R8",
        "productImage": "mainimage.jpg",
        "currentUnitPrice": 20000.00,
        "quantity": 1,
        "totalPrice": 20000.00,
        "createTime": "2016-11-30 22:23:49"
      }
    ],
    "imageHost": "http://img.happymmall.com/",
    "shippingId": 3,
    "receiverName": "geely",
    "shippingVo": {
      "receiverName": "geely",
      "receiverPhone": "0100",
      "receiverMobile": "186",
      "receiverProvince": "北京",
      "receiverCity": "北京",
      "receiverDistrict": "昌平区",
      "receiverAddress": "矩阵小区",
      "receiverZip": "100000"
    }
  }
}

```

fail
```
{
  "status": 1,
  "msg": "没有找到订单"
}
```

------

####4.订单发货

http://localhost:8080/manage/order/send_goods.do?orderNo=1480515829406


**/manage/order/send_goods.do**

> request

```
orderNo
```

> response

success

```
{
  "status": 0,
  "data": "发货成功"
}

```

fail
```
{
  "status": 1,
  "msg": "发货失败"
}
```

------
[MENU]


####1.产品搜索及动态排序List

**/product/list.do**

http://localhost:8080/product/list.do?keyword=&categoryId=1&orderBy=price_desc

> request

```
categoryId
keyword
pageNum(default=1)
pageSize(default=10)
orderBy(default="")：排序参数：例如price_desc，price_asc

```

> response

success

```
{
    "status": 0,
    "data": {
        "pageNum": 1,
        "pageSize": 10,
        "size": 2,
        "orderBy": null,
        "startRow": 1,
        "endRow": 2,
        "total": 2,
        "pages": 1,
        "list": [
            {
                "id": 1,
                "categoryId": 3,
                "name": "iphone7",
                "subtitle": "双十一促销",
                "mainImage": "mainimage.jpg",
                "status":1,
                "price": 7199.22
            },
            {
                "id": 2,
                "categoryId": 2,
                "name": "oppo R8",
                "subtitle": "oppo促销进行中",
                "mainImage": "mainimage.jpg",
                "status":1,
                "price": 2999.11
            }
        ],
        "firstPage": 1,
        "prePage": 0,
        "nextPage": 0,
        "lastPage": 1,
        "isFirstPage": true,
        "isLastPage": true,
        "hasPreviousPage": false,
        "hasNextPage": false,
        "navigatePages": 8,
        "navigatepageNums": [
            1
        ]
    }
}
```

fail
```
{
    "status": 1,
    "msg": "参数错误"
}
```


------

####2.产品detail

**/product/detail.do**

http://localhost:8080/product/detail.do?productId=2

> request

```
productId
```

> response

success

```
{
  "status": 0,
  "data": {
    "id": 2,
    "categoryId": 2,
    "name": "oppo R8",
    "subtitle": "oppo促销进行中",
    "mainImage": "mainimage.jpg",
    "subImages": "[\"mmall/aa.jpg\",\"mmall/bb.jpg\",\"mmall/cc.jpg\",\"mmall/dd.jpg\",\"mmall/ee.jpg\"]",
    "detail": "richtext",
    "price": 2999.11,
    "stock": 71,
    "status": 1,
    "createTime": "2016-11-20 14:21:53",
    "updateTime": "2016-11-20 14:21:53"
  }
}

```

fail
```
{
    "status": 1,
    "msg": "该商品已下架或删除"
}
```


------

####模板

**/product/.do**

> request

```
k
```

> response

success

```
k
```

fail
```
k
```


------
[MENU]



------

####1.支付

**/order/pay.do**

http://localhost:8080/order/pay.do?orderNo=1485158676346


> request

```
orderNo
```

> response

success

```
{
    "status": 0,
    "data": {
        "orderNo": "1485158676346",
        "qrPath": "http://img.happymmall.com/qr-1492329044075.png"
    }
}
```

fail
```
{
    "status": 1,
    "msg": "支付宝生成订单失败"
}
```

------

####2.查询订单支付状态

**/order/query_order_pay_status.do**

http://localhost:8080/order/query_order_pay_status.do?orderNo=1485158676346


> request

```
orderNo
```

> response

success

```
{
    "status": 0,
    "data": true
}


```

fail
```
{
    "status": 1,
    "msg": "该用户并没有该订单,查询无效"
}
```

------


####3.支付宝回调

参考支付宝回调文档：
https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.mFogPC&treeId=193&articleId=103296&docType=1

**/order/alipay_callback.do**

> request

```
HttpServletRequest
```

> response

success

```
success
```

fail
```
failed
```

------


####0.模板

**/REPLACE/.do**

> request

```
k
```

> response

success

```
k
```

fail
```
k
```

------
[MENU]


####1.添加地址

**/shipping/add.do**

http://localhost:8080/shipping/add.do?userId=1&receiverName=geely&receiverPhone=010&receiverMobile=18688888888&receiverProvince=%E5%8C%97%E4%BA%AC&receiverCity=%E5%8C%97%E4%BA%AC%E5%B8%82&receiverAddress=%E4%B8%AD%E5%85%B3%E6%9D%91&receiverZip=100000

> request

```
userId=1
receiverName=geely
receiverPhone=010
receiverMobile=18688888888
receiverProvince=北京
receiverCity=北京市
receiverAddress=中关村
receiverZip=100000

```

> response

success

```
{
    "status": 0,
    "msg": "新建地址成功",
    "data": {
        "shippingId": 28
    }
}
```

fail
```
{
    "status": 1,
    "msg": "新建地址失败"
}
```


------


####2.删除地址

**/shipping/del.do**

> request

```
shippingId
```

> response

success

```
{
    "status": 0,
    "msg": "删除地址成功"
}
```

fail
```
{
    "status": 1,
    "msg": "删除地址失败"
}
```


------


####3.登录状态更新地址

**/shipping/update.do**

http://localhost:8080/shipping/update.do?id=5&receiverName=AAA&receiverPhone=010&receiverMobile=18688888888&receiverProvince=%E5%8C%97%E4%BA%AC&receiverCity=%E5%8C%97%E4%BA%AC%E5%B8%82&receiverDistrict=%E6%B5%B7%E6%B7%80%E5%8C%BA&receiverAddress=%E4%B8%AD%E5%85%B3%E6%9D%91&receiverZip=100000

> request

```
id=1
receiverName=geely
receiverPhone=010
receiverMobile=18688888888
receiverProvince=北京
receiverCity=北京市
receiverAddress=中关村
receiverZip=100000
```

> response

success

```
{
    "status": 0,
    "msg": "更新地址成功"
}
```

fail
```
{
    "status": 1,
    "msg": "更新地址失败"
}
```


------


####4.选中查看具体的地址

**/shipping/select.do**

> request

```
shippingId
```

> response

success

```
{
    "status": 0,
    "data": {
        "id": 4,
        "userId": 13,
        "receiverName": "geely",
        "receiverPhone": "010",
        "receiverMobile": "18688888888",
        "receiverProvince": "北京",
        "receiverCity": "北京市",
        "receiverAddress": "中关村",
        "receiverZip": "100000",
        "createTime": 1485066385000,
        "updateTime": 1485066385000
    }
}
```

fail
```
{
    "status": 1,
    "msg": "请登录之后查询"
}
```


------


####5.地址列表

**/shipping/list.do**

http://localhost:8080/shipping/list.do

> request

```
pageNum(默认1),pageSize(默认10)
```

> response

success

```
{
    "status": 0,
    "data": {
        "pageNum": 1,
        "pageSize": 10,
        "size": 2,
        "orderBy": null,
        "startRow": 1,
        "endRow": 2,
        "total": 2,
        "pages": 1,
        "list": [
            {
                "id": 4,
                "userId": 13,
                "receiverName": "geely",
                "receiverPhone": "010",
                "receiverMobile": "18688888888",
                "receiverProvince": "北京",
                "receiverCity": "北京市",
                "receiverAddress": "中关村",
                "receiverZip": "100000",
                "createTime": 1485066385000,
                "updateTime": 1485066385000
            },
            {
                "id": 5,
                "userId": 13,
                "receiverName": "AAA",
                "receiverPhone": "010",
                "receiverMobile": "18688888888",
                "receiverProvince": "北京",
                "receiverCity": "北京市",
                "receiverAddress": "中关村",
                "receiverZip": "100000",
                "createTime": 1485066392000,
                "updateTime": 1485075875000
            }
        ],
        "firstPage": 1,
        "prePage": 0,
        "nextPage": 0,
        "lastPage": 1,
        "isFirstPage": true,
        "isLastPage": true,
        "hasPreviousPage": false,
        "hasNextPage": false,
        "navigatePages": 8,
        "navigatepageNums": [
            1
        ]
    }
}
```

fail
```
{
    "status": 1,
    "msg": "请登录之后查询"
}
```


------





####模板

**/product/.do**

> request

```
k
```

> response

success

```
k
```

fail
```
k
```


------
[MENU]

####1.登录


**/user/login.do**  post(代码需要post方式请求),开放get，方便调试

> request

```
username,password
```
> response

fail
```
{
    "status": 1,
    "msg": "密码错误"
}
```

success
```
{
    "status": 0,
    "data": {
        "id": 12,
        "username": "aaa",
        "email": "aaa@163.com",
        "phone": null,
        "role": 0,
        "createTime": 1479048325000,
        "updateTime": 1479048325000
    }
}
```


-------

####2.注册
**/user/register.do**

> request

```
username,password,email,phone,question,answer
```


> response

success
```
{
    "status": 0,
    "msg": "校验成功"
}
```


fail
```
{
    "status": 1,
    "msg": "用户已存在"
}
```


--------

####3.检查用户名是否有效

**/user/check_valid.do**

/check_valid.do?str=admin&type=username就是检查用户名。



> request

```
str,type
str可以是用户名也可以是email。对应的type是username和email

```

>response

success
```
{
    "status": 0,
    "msg": "校验成功"
}

```

fail
```
{
    "status": 1,
    "msg": "用户已存在"
}

```


-----------


####4.获取登录用户信息
**/user/get_user_info.do**


> request

```
无参数
```
> response

success
```
{
    "status": 0,
    "data": {
        "id": 12,
        "username": "aaa",
        "email": "aaa@163.com",
        "phone": null,
        "role": 0,
        "createTime": 1479048325000,
        "updateTime": 1479048325000
    }
}
```

fail
```
{
    "status": 1,
    "msg": "用户未登录,无法获取当前用户信息"
}

```


------

####5.忘记密码
**/user/forget_get_question.do**

localhost:8080/user/forget_get_question.do?username=geely



> request

```
username
```
> response

success

```
{
    "status": 0,
    "data": "这里是问题"
}
```

fail
```
{
    "status": 1,
    "msg": "该用户未设置找回密码问题"
}
```


---------

####6.提交问题答案
**/user/forget_check_answer.do**

localhost:8080/user/forget_check_answer.do?username=aaa&question=aa&answer=sss


> request

```
username,question,answer
```

> response

正确的返回值里面有一个token，修改密码的时候需要用这个。传递给下一个接口



success

```
{
    "status": 0,
    "data": "531ef4b4-9663-4e6d-9a20-fb56367446a5"
}
```

fail

```
{
    "status": 1,
    "msg": "问题答案错误"
}
```


------

####7.忘记密码的重设密码
**/user/forget_reset_password.do**

localhost:8080/user/forget_reset_password.do?username=aaa&passwordNew=xxx&forgetToken=531ef4b4-9663-4e6d-9a20-fb56367446a5

> request

```
username,passwordNew,forgetToken
```

> response

success

```
{
    "status": 0,
    "msg": "修改密码成功"
}
```

fail
```
{
    "status": 1,
    "msg": "修改密码操作失效"
}
```
或
```
{
    "status": 1,
    "msg": "token已经失效"
}
```


------
####8.登录中状态重置密码
**/user/reset_password.do**

> request

```
passwordOld,passwordNew

```

> response

success

```
{
    "status": 0,
    "msg": "修改密码成功"
}
```

fail
```
{
    "status": 1,
    "msg": "旧密码输入错误"
}
```

------
####9.登录状态更新个人信息
**/user/update_information.do**

> request

```
email,phone,question,answer
```

> response

success

```
{
    "status": 0,
    "msg": "更新个人信息成功"
}
```

fail
```
{
    "status": 1,
    "msg": "用户未登录"
}
```


------
####10.获取当前登录用户的详细信息，并强制登录
**/user/get_information.do**


> request

```
无参数
```
> response

success
```
{
    "status": 0,
    "data": {
        "id": 1,
        "username": "admin",
        "password": "",
        "email": "admin@163.com",
        "phone": "13800138000",
        "question": "question",
        "answer": "answer",
        "role": 1,
        "createTime": 1478422605000,
        "updateTime": 1491305256000
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,无法获取当前用户信息,status=10,强制登录"
}

```


------


####11.退出登录
**/user/logout.do**

> request

```
无
```

> response

success

```
{
    "status": 0,
    "msg": "退出成功"
}
```

fail
```
{
    "status": 1,
    "msg": "服务端异常"
}
```


------



####模板

**/user/.do**

> request

```
k
```

> response

success

```
k
```

fail
```
k
```


------

[MENU]

------

####1.创建订单

**/order/create.do**

引用已存在的收货地址id
http://localhost:8080/order/create.do?shippingId=5


> request

```
shippingId
```

> response

success

```
{
    "status": 0,
    "data": {
        "orderNo": 1485158223095,
        "payment": 2999.11,
        "paymentType": 1,
        "postage": 0,
        "status": 10,
        "paymentTime": null,
        "sendTime": null,
        "endTime": null,
        "closeTime": null,
        "createTime": 1485158223095,
        "orderItemVoList": [
            {
                "orderNo": 1485158223095,
                "productId": 2,
                "productName": "oppo R8",
                "productImage": "mainimage.jpg",
                "currentUnitPrice": 2999.11,
                "quantity": 1,
                "totalPrice": 2999.11,
                "createTime": null
            }
        ],
        "shippingId": 5,
        "shippingVo": null
    }
}
```

fail
```
{
    "status": 1,
    "msg": "创建订单失败"
}
```

------

####2.获取订单的商品信息

**/order/get_order_cart_product.do**

http://localhost:8080/order/get_order_cart_product.do


> request

```
无
```

> response

success

```
{
    "status": 0,
    "data": {
        "orderItemVoList": [
            {
                "orderNo": null,
                "productId": 1,
                "productName": "iphone7",
                "productImage": "mmall/aa.jpg",
                "currentUnitPrice": 7999,
                "quantity": 10,
                "totalPrice": 79990,
                "createTime": ""
            }
        ],
        "imageHost": "http://img.happymmall.com/",
        "productTotalPrice": 79990
    }
}
```

fail
```
{
    "status": 1,
    "msg": "用户未登录"
}
```

####3.订单List

http://localhost:8080/order/list.do?pageSize=3

**/order/list.do**

> request

```
pageSize(default=10)
pageNum(default=1)
```

> response

success

```
{
  "status": 0,
  "data": {
    "pageNum": 1,
    "pageSize": 3,
    "size": 3,
    "orderBy": null,
    "startRow": 1,
    "endRow": 3,
    "total": 16,
    "pages": 6,
    "list": [
      {
        "orderNo": 1485158676346,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:36",
        "orderItemVoList": [
          {
            "orderNo": 1485158676346,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:36"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "receiverName": "geely",
        "shippingVo": null
      },
      {
        "orderNo": 1485158675516,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:35",
        "orderItemVoList": [
          {
            "orderNo": 1485158675516,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:35"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "receiverName": "geely",
        "shippingVo": null
      },
      {
        "orderNo": 1485158675316,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2017-02-11 12:27:18",
        "sendTime": "2017-02-11 12:27:18",
        "endTime": "2017-02-11 12:27:18",
        "closeTime": "2017-02-11 12:27:18",
        "createTime": "2017-01-23 16:04:35",
        "orderItemVoList": [
          {
            "orderNo": 1485158675316,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2017-01-23 16:04:35"
          }
        ],
        "imageHost": "http://img.happymmall.com/",
        "shippingId": 5,
        "receiverName": "geely",
        "shippingVo": null
      }
    ],
    "firstPage": 1,
    "prePage": 0,
    "nextPage": 2,
    "lastPage": 6,
    "isFirstPage": true,
    "isLastPage": false,
    "hasPreviousPage": false,
    "hasNextPage": true,
    "navigatePages": 8,
    "navigatepageNums": [
      1,
      2,
      3,
      4,
      5,
      6
    ]
  }
}
```

fail
```
{
  "status": 10,
  "msg": "用户未登录,请登录"
}


或

{
  "status": 1,
  "msg": "没有权限"
}



```

------

####4.订单详情detail

http://localhost:8080/order/detail.do?orderNo=1480515829406

**/order/detail.do**

> request

```
orderNo
```

> response

success

```
{
  "status": 0,
  "data": {
    "orderNo": 1480515829406,
    "payment": 30000.00,
    "paymentType": 1,
    "paymentTypeDesc": "在线支付",
    "postage": 0,
    "status": 10,
    "statusDesc": "未支付",
    "paymentTime": "",
    "sendTime": "",
    "endTime": "",
    "closeTime": "",
    "createTime": "2016-11-30 22:23:49",
    "orderItemVoList": [
      {
        "orderNo": 1480515829406,
        "productId": 1,
        "productName": "iphone7",
        "productImage": "mainimage.jpg",
        "currentUnitPrice": 10000.00,
        "quantity": 1,
        "totalPrice": 10000.00,
        "createTime": "2016-11-30 22:23:49"
      },
      {
        "orderNo": 1480515829406,
        "productId": 2,
        "productName": "oppo R8",
        "productImage": "mainimage.jpg",
        "currentUnitPrice": 20000.00,
        "quantity": 1,
        "totalPrice": 20000.00,
        "createTime": "2016-11-30 22:23:49"
      }
    ],
    "imageHost": "http://img.happymmall.com/",
    "shippingId": 3,
    "receiverName": "geely",
    "shippingVo": {
      "receiverName": "geely",
      "receiverPhone": "0100",
      "receiverMobile": "186",
      "receiverProvince": "北京",
      "receiverCity": "北京",
      "receiverDistrict": "昌平区",
      "receiverAddress": "矩阵小区",
      "receiverZip": "100000"
    }
  }
}

```

fail
```
{
  "status": 1,
  "msg": "没有找到订单"
}
```

------

####5.取消订单

http://localhost:8080/order/cancel.do?orderNo=1480515829406

**/order/cancel.do**

> request

```
orderNo
```

> response

success

```
{
  "status": 0
}

```

fail
```
{
  "status": 1,
  "msg": "该用户没有此订单"
}

或
{
  "status": 1,
  "msg": "此订单已付款，无法被取消"
}
```

------



####5.模板

**/REPLACE/.do**

> request

```
k
```

> response

success

```
k
```

fail
```
k
```

------
[MENU]




####1.购物车List列表

**/cart/list.do**

http://localhost:8080/cart/list.do

注意点：
1. 需要先登录,所有的密码都是123
2. NEED_LOGIN(10, "NEED_LOGIN"),//需要登录的错误编码
3. 价格的单位是元,保留小数后2位


> request

```
无参数,需要登录状态
```

> response

success

```

{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "id": 1,
                "userId": 13,
                "productId": 1,
                "quantity": 1,
                "productName": "iphone7",
                "productSubtitle": "双十一促销",
                "productMainImage": "mainimage.jpg",
                "productPrice": 7199.22,
                "productStatus": 1,
                "productTotalPrice": 7199.22,
                "productStock": 86,
                "productChecked": 1,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            },
            {
                "id": 2,
                "userId": 13,
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productChecked": 1,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            }
        ],
        "allChecked": true,
        "cartTotalPrice": 10198.33
    }
}

```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------





####2.购物车添加商品

**/cart/add.do**

http://localhost:8080/cart/add.do?productId=1&count=10

请注意这个字段，超过数量会返回这样的标识"limitQuantity"

失败的：LIMIT_NUM_FAIL
成功的：LIMIT_NUM_SUCCESS


> request

```
productId,count
```

> response

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "id": 1,
                "userId": 13,
                "productId": 1,
                "quantity": 12,
                "productName": "iphone7",
                "productSubtitle": "双十一促销",
                "productMainImage": "mainimage.jpg",
                "productPrice": 7199.22,
                "productStatus": 1,
                "productTotalPrice": 86390.64,
                "productStock": 86,
                "productChecked": 1,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            },
            {
                "id": 2,
                "userId": 13,
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productChecked": 1,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            }
        ],
        "allChecked": true,
        "cartTotalPrice": 89389.75
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------





####3.更新购物车某个产品数量

**/cart/update.do**

http://localhost:8080/cart/update.do?productId=1&count=2

> request

```
productId,count
```

> response

响应同2

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "id": 1,
                "userId": 13,
                "productId": 1,
                "quantity": 12,
                "productName": "iphone7",
                "productSubtitle": "双十一促销",
                "productMainImage": "mainimage.jpg",
                "productPrice": 7199.22,
                "productStatus": 1,
                "productTotalPrice": 86390.64,
                "productStock": 86,
                "productChecked": 1,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            },
            {
                "id": 2,
                "userId": 13,
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productChecked": 1,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            }
        ],
        "allChecked": true,
        "cartTotalPrice": 89389.75
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------





####4.移除购物车某个产品

**/cart/delete_product.do**

http://localhost:8080/cart/delete_product.do?productIds=1,3

> request

```
productIds
```

> response

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "id": 2,
                "userId": 13,
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productChecked": 1,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            }
        ],
        "allChecked": true,
        "cartTotalPrice": 2999.11
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------





####5.购物车选中某个商品

**/cart/select.do**

http://localhost:8080/cart/select.do?productId=1


> request

```
productId
```

> response

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "id": 2,
                "userId": 13,
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productChecked": 1,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            }
        ],
        "allChecked": true,
        "cartTotalPrice": 2999.11
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------





####6.购物车取消选中某个商品

**/cart/un_select.do**

http://localhost:8080/cart/un_select.do?productId=2

> 注意返回值中的cartTotalPrice，如果反选之后总价的变化

> request

```
productId
```

> response

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "id": 2,
                "userId": 13,
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productChecked": 0,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            }
        ],
        "allChecked": true,
        "cartTotalPrice": 0
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------






####7.查询在购物车里的产品数量

**/cart/get_cart_product_count.do**

http://localhost:8080/cart/get_cart_product_count.do

> 未登录返回0

> request

```
无
```

> response

success

```
{
    "status": 0,
    "data": 0

}
```

fail
```
{
    "status": 10,
    "msg": "出现异常"
}
```


------





####8.购物车全选

**/cart/select_all.do**

http://localhost:8080/cart/select_all.do

> 注意返回值中的cartTotalPrice的变化

> request

```
无
```

> response

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "id": 2,
                "userId": 13,
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productChecked": 0,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            }
        ],
        "allChecked": true,
        "cartTotalPrice": 2999.11
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------





####9.购物车取消全选

**/cart/un_select_all.do**

http://localhost:8080/cart/un_select_all.do

> 注意返回值中的cartTotalPrice总价的变化

> request

```
无
```

> response

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "id": 2,
                "userId": 13,
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productChecked": 0,
                "limitQuantity": "LIMIT_NUM_SUCCESS"
            }
        ],
        "allChecked": true,
        "cartTotalPrice": 0
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------


*一次整合练手，获益良多！！！ 学习踩坑记录在[这里](qroxyquan.github.com)!!,欢迎访问。*