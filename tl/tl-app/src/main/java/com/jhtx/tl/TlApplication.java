package com.jhtx.tl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class TlApplication {

    public static void main(String[] args) {
        SpringApplication.run(TlApplication.class, args);
        log.info("----------------------服务启动成功--------------------");
    }
}
