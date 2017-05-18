package uk.co.malbec.welcometohell;


import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import javax.servlet.Servlet;
import java.util.Properties;


@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@SuppressWarnings({"deprecation"})
public class WelcomeToHellApplication {
    public static void main(String[] args) {
        SpringApplication.run(WelcomeToHellApplication.class, args);
    }

    @Bean("widgetEngine")
    public VelocityEngine templateEngine(){

        VelocityEngine velocityEngine = new VelocityEngine();

        Properties props = new Properties();
        String path = System.getProperty("user.dir") + "/src/main/resources/views/";

        props.put("file.resource.loader.path", path);

        //TODO - get alternative configuration that loads from classpath.

        velocityEngine.init(props);

        return velocityEngine;
    }

    @Bean
    VelocityConfigurer velocityConfig() {
        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
        velocityConfigurer.setResourceLoaderPath("classpath:/public/");
        return velocityConfigurer;
    }

    @Bean
    VelocityViewResolver velocityViewResolver() {
        VelocityLayoutViewResolver resolver = new VelocityLayoutViewResolver();
        resolver.setSuffix(".vm");
        resolver.setPrefix("/templates/");
        resolver.setLayoutUrl("/templates/template.vm");
        return resolver;
    }

}
