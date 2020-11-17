package cn.j.netstorage.Config;

import cn.j.netstorage.Entity.File.HardDiskDevice;
import cn.j.netstorage.Mapper.FilesVersionMapper;
import cn.j.netstorage.Mapper.HardDeviceMapper;
import cn.j.netstorage.tool.FilesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Component
public class ResourceAdpater implements WebMvcConfigurer {
    //自定义的拦截器

    @Autowired
    HardDeviceMapper hardDeviceMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器，添加拦截路径和排除拦截路径
//        registry.addInterceptor(customerInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login","/","/image/**","/error");
    }

    @Value("${ip}")
    private String ip;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        hardDeviceMapper.findAll().forEach((value)->{
            registry.addResourceHandler(String.format("%s/**", value.getCustomName())).addResourceLocations(String.format("file:%s\\", value.getFolderName()));
        });
    }
}
