package com.furing.chatgroupauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author furing
 */
@ServletComponentScan("com.furing.chatgroupauth.interceptor.AuthInterceptor")
@SpringBootApplication
public class ChatGroupAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatGroupAuthApplication.class, args);
    }

}
