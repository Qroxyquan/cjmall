package com.qroxy.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author: Qroxy
 * *
 * @QQ：1114031075 *
 * @时间: 2018/9/28-9:01 PM
 */
public class Const {
    public static  final String CURRENT_USER="currentUser";
    public static final String USERNAME="username";
    public static final String EMAIL="email";

    public interface Role{
        //普通用户
        int ROLE_COSTOMER = 0;
        //管理员
        int ROLE_ADMIN = 1;

    }

    public interface Cart {

        int CHECKED = 1;//购物车选中状态
        int UNCHECKED = 0;//购物车未选中状态
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";

    }
    public enum productStatusEnum {
        ON_SALE(1, "在线");


        private String value;

        public String getValue() {
            return value;
        }


        public int getCode() {
            return code;
        }


        private int code;

        productStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;

        }

    }

    public interface ProductListOrderBy {
        /**
         * @desc:使用set不使用list的原因是因为，set的时间复杂度为O（1）。而list为O(n)
         * @author:Qroxy
         * @date:2018/10/10 12:28 PM
         * @param:
         * @type:
         */
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");

    }
}
