package com.example.course_project.security;

import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.userInfo.costumAuth.CostumClient;
import com.example.course_project.userInfo.costumAuth.CostumService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {


    private final BCryptPasswordEncoder encoder;
    private final UserService service;
    private final CostumService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/signup").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/collections/item/*").permitAll()
                .antMatchers("/tag/search/*").permitAll()
                .antMatchers("/collections/*").permitAll()
                .antMatchers("/all/*").permitAll()
                .antMatchers("/tag/search/data/collection/").permitAll()
                .antMatchers("/tag/search/data/").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/all/1")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(userService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        CostumClient user = (CostumClient) authentication.getPrincipal();

                        service.processCostumUser(user.getName(),user.getUsername(),response);
                        response.sendRedirect("/all/1");
                    }
                });


    }

    @Bean
    public DaoAuthenticationProvider provider(){
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(service);
        return provider;
    }
}
