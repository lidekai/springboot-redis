package com.ledtech.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @ClassName:JavaApplication
 * @Author:DK_Li
 * @Date:Created in 2019/5/2 0:27
 * @Description:应用入口
 * @Version:1.0
 */

@SpringBootApplication(scanBasePackages ={"com.ledtech.java.*"})
public class JavaApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JavaApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JavaApplication.class);
    }

}