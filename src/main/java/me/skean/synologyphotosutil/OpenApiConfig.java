package me.skean.synologyphotosutil;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 标明是配置类
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info().title("SynologyPhotosUtil").version("1.0.0").description("SynologyPhotos图片管理工具"));
    }
}
