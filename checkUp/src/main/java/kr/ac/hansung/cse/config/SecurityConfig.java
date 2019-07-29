package kr.ac.hansung.cse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// xml, annotation, java class로 설정하는 방법 중 java class로 설정!!
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http        
        	.authorizeRequests()
        		.anyRequest()
        		.permitAll()
        		.and()
            .csrf().disable();
    }
}