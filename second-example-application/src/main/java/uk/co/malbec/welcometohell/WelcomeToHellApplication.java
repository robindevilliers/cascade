package uk.co.malbec.welcometohell;


import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;


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
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

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
