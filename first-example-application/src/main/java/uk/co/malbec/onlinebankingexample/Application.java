package uk.co.malbec.onlinebankingexample;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import javax.servlet.Servlet;
import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

       /* System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/
    }

    @Configuration
    @ConditionalOnClass({Servlet.class})
    @AutoConfigureAfter(WebMvcAutoConfiguration.class)
    static class VelocityConfiguration implements EnvironmentAware {
        private RelaxedPropertyResolver environment;

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = new RelaxedPropertyResolver(environment, "spring.velocity.");
        }
        @Bean
        VelocityConfigurer velocityConfig() {
            VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
            velocityConfigurer.setResourceLoaderPath("file:./first-example-application/src/main/resources");
            return velocityConfigurer;
        }
        @Bean
        VelocityViewResolver velocityViewResolver() {
            VelocityLayoutViewResolver resolver = new VelocityLayoutViewResolver();
            resolver.setSuffix(".vm");
            resolver.setPrefix("/templates/");
            resolver.setLayoutUrl("template.vm");
            return resolver;
        }
    }

}
