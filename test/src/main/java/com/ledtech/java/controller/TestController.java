package com.ledtech.java.controller;



import com.ledtech.java.util.SyniRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName:TestController
 * @Author:DK_Li
 * @Date:Created in 2019/5/3 13:08
 * @Description:测试的控制层
 * @Version:1.0
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SyniRedisTemplate syniRedisTemplate;


    @RequestMapping("/redis")
    public String testRedis() {
        System.out.println("11111111");
        System.out.println(syniRedisTemplate.get("key","2"));
        return "index";
    }
}
