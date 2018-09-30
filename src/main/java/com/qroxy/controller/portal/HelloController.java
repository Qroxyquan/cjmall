package com.qroxy.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: Qroxy
 * *
 * @QQ：1114031075 *
 * @时间: 2018/9/29-11:14 PM
 */
@Controller
public class HelloController {




@RequestMapping("/hello")
    public String Hello(){

        return "index";
    }
}
