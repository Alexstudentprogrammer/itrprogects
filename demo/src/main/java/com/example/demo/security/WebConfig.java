package com.example.demo.security;

import com.example.demo.UserInfo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {

    private final UserService service;
    private final BCryptPasswordEncoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

         http
                 .csrf().disable()
                 .authorizeRequests()
                 .antMatchers("/api/signup").permitAll()
                 .antMatchers("/api/signup/failed").permitAll()
                 .antMatchers("/login").permitAll()
                 .anyRequest()
                 .authenticated()
                 .and()
                 .formLogin()
                 .loginPage("/login")
                 .defaultSuccessUrl("https://itrproject1.herokuapp.com/getAll");

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
