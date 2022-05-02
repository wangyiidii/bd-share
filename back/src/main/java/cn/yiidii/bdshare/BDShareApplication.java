package cn.yiidii.bdshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 程序入口
 *
 * @author YiiDii Wang
 * @create 2021-11-25 10:09
 */
@SpringBootApplication
@ComponentScan(basePackages = "cn.yiidii")
public class BDShareApplication {

    public static void main(String[] args) {
        SpringApplication.run(BDShareApplication.class, args);
    }
}
