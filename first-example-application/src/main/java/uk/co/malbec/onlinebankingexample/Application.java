package uk.co.malbec.onlinebankingexample;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.joda.time.DateTime;
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
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;
import uk.co.malbec.onlinebankingexample.model.Account;
import uk.co.malbec.onlinebankingexample.model.AccountType;
import uk.co.malbec.onlinebankingexample.model.User;
import uk.co.malbec.onlinebankingexample.tools.TestTool;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.joda.time.DateTime.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }


    private NumberTool numberTool = new NumberTool();
    private MathTool mathTool = new MathTool();
    private EscapeTool escapeTool = new EscapeTool();
    private DateTool dateTool = new DateTool();
    private MoneyTool moneyTool = new MoneyTool();

    @Bean
    ObjectMapper objectMapper(){

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                if (jp.getText().trim().isEmpty()){
                    return null;
                }
                return parse(jp.getText(), forPattern("dd MMM yyyy")).toDate();
            }
        });
        mapper.registerModule(module);
        return mapper;
    }

    @Bean
    WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HandlerInterceptorAdapter() {

                    List<String> publicUris = new ArrayList<String>() {{
                        add("/");
                        add("/authenticate");
                        add("/answer");
                        add("/database/set-user");
                        add("/error");

                    }};

                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                            throws Exception {

                        if (!publicUris.contains(request.getRequestURI())) {
                            if (request.getSession().getAttribute("user") == null || request.getSession().getAttribute("authenticated") == null) {
                                response.sendRedirect("/");
                                return false;
                            }
                        }
                        return true;
                    }

                    @Override
                    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                        if (modelAndView == null) {
                            modelAndView = new ModelAndView();
                        }

                        User user = (User) request.getSession().getAttribute("user");
                        if (user != null && user.getAccounts() != null){
                            boolean currentAccountPresent = false;
                            for (Account account: user.getAccounts()){
                                if (account.getType().equals(AccountType.Current)){
                                    currentAccountPresent = true;
                                    break;
                                }
                            }
                            modelAndView.addObject("currentAccountPresent", currentAccountPresent);
                        }

                        modelAndView.addObject("number", numberTool);
                        modelAndView.addObject("escape", escapeTool);
                        modelAndView.addObject("math", mathTool);
                        modelAndView.addObject("date", dateTool);
                        modelAndView.addObject("money", moneyTool);
                    }
                });
            }
        };
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


    public class DateTool {
        public String format(Object date) {
            if (date == null) {
                return "";
            }
            return new DateTime(date).toString("dd MMM yyyy");
        }
    }

    public class MoneyTool {
        public String format(Integer money) {
            String str = numberTool.format("###,###,##0.00", mathTool.div(mathTool.abs(money), 100));
            if (money >= 0){
                return String.format("£  %s", str);
            } else {
                return String.format("£ (%s)", str);
            }
        }
    }
}
