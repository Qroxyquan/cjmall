package com.qroxy.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/10-12:52 PM
 */
public class BigDecimaTest {
    @Test
    public  void test1(){
        System.out.println(0.05+0.01);
        System.out.println(1.01-0.42);
        System.out.println(4.015*100);
        System.out.println(123.3/100);
        BigDecimal b1=new BigDecimal(0.05);
        BigDecimal b2=new BigDecimal(0.03);
        System.out.println(b1.add(b2));

    }
    @Test
    public  void test2(){
        BigDecimal b1=new BigDecimal(0.05);
        BigDecimal b2=new BigDecimal(0.03);
        System.out.println(b1.add(b2));


    }
    @Test
    public  void test3(){
//        一定使用string构造器，不然出现test2情况
        BigDecimal b1=new BigDecimal("0.05");
        BigDecimal b2=new BigDecimal("0.03");
        System.out.println(b1.add(b2));


    }
}
