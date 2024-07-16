package com.pig4cloud.pig.patient;

import com.pig4cloud.pig.common.security.annotation.EnablePigResourceServer;
import com.pig4cloud.pig.common.swagger.annotation.EnablePigDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* @author pig archetype
* <p>
* 项目启动类
*/
@EnablePigDoc("pig")
@EnablePigResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class PatientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientApplication.class, args);
    }

}
