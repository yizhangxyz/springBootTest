package game.SpringBoot.controller;


import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport
{
	 @Autowired
	 private Environment env;
	 
	//资源路径------------------------------------------
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations(env.getProperty("web.path"));
        super.addResourceHandlers(registry);
    }
	
	//编码格式------------------------------------------
	@Bean
    public HttpMessageConverter<String> responseBodyConverter()
	{
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) 
    {
        converters.add(responseBodyConverter());
        addDefaultHttpMessageConverters(converters);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) 
    {
        configurer.favorPathExtension(false);
    }
    
    //拦截器------------------------------------------
    @Bean
    public SecurityInterceptor getSecurityInterceptor() 
    {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry)
    {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter
    {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
        	
            return true;
        }
    }
}