package com.world.navigator.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/world-navigator")
            .permitAll()
            .antMatchers("/signup")
            .permitAll()
            .and()
            .requiresChannel()
            .anyRequest()
            .requiresSecure()
            .and()
            .csrf()
            .disable();
  }
}
