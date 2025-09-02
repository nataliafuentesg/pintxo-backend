package com.pintxo.configurations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Value("${tasca.cors.origins:}")
//    String originsCsv;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        String[] fromProps = Arrays.stream(originsCsv.split("\\s*,\\s*"))
//                .filter(s -> !s.isBlank())
//                .toArray(String[]::new);
//
//        String[] fallback = new String[] {
//                "https://tascatapas.com",
//                "https://www.tascatapas.com",
//                "http://localhost:5175",
//                "http://127.0.0.1:5175"
//        };
//
//        String[] allowed = (fromProps.length > 0) ? fromProps : fallback;
//
//        registry.addMapping("/**")
//                .allowedOrigins(allowed)
//                .allowedOriginPatterns("https://*.netlify.app")
//                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
//                .allowedHeaders("*")
//                .exposedHeaders("Authorization","Location","Content-Disposition")
//                .allowCredentials(true);
//    }
}




