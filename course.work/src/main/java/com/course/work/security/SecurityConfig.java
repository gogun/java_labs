package com.course.work.security;

import com.course.work.security.jwt.JwtSecurityConfigurer;
import com.course.work.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/goods/add").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/sales/delete/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/warehouse/add").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/goods/delete/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/goods/update").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/warehouse/update").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/warehouse/update/**").hasRole("ADMIN")
                .antMatchers("/api/auth/signin").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfigurer(jwtTokenProvider));

    }
}
