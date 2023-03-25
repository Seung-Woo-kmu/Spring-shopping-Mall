package woo.shoppingMall.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import woo.shoppingMall.web.interceptor.AdminCheckInterceptor;
import woo.shoppingMall.web.interceptor.CacheInterceptor;
import woo.shoppingMall.web.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminCheckInterceptor())
                .order(1)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login", "/", "/addUser", "/login", "/logout",
                        "/mailCheck","/css/**", "/error", "/shoes/**",
                        "/assets/favicon.ico", "/admin/assets/favicon.ico", "/js/scripts.js",
                        "/favicon.ico", "/itemList", "/items/**", "/find/**");
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/addUser", "/login", "/logout",
                        "/mailCheck","/css/**", "/error", "/shoes/**",
                        "/assets/favicon.ico", "/js/scripts.js",
                        "/favicon.ico", "/itemList", "/items/**", "/find/**", "/admin/**", "//shoes/**");
        registry.addInterceptor(new CacheInterceptor())
                .order(3)
                .addPathPatterns("/**");
    }
}
