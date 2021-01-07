package cn.j.netstorage.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.MultipartConfigElement;

@Configuration
public class ResourceTmp {

    @Value("${server.tomcat.basedir}")
    private String baseDir;

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        System.out.println("========================Tomcat Dir==================");
        System.out.println(baseDir);
        factory.setLocation(baseDir);
        return factory.createMultipartConfig();
    }
}
