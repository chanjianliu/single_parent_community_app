package com.team9.spda_team9.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Qualifier("loginService") //since we are not using in memory authentication
    @Autowired
    private UserDetailsService userDetailsService;

   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService);

   }


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .csrf()
                	.disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/vendor/**").permitAll()
                	.antMatchers("/getcomments").permitAll()
                	.antMatchers("/getusers").permitAll()
                	.anyRequest().authenticated()
                	.and()
                .formLogin()
                	.loginPage("/login")
                	.permitAll()
                	.and()
                .logout()
                	.permitAll();
    }
		
	@Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }
}
