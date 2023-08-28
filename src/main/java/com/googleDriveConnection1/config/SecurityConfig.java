//package com.googleDriveConnection1.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//        .authorizeRequests()
//        .antMatchers("/public/**").permitAll() // Publicly accessible URLs
//        .antMatchers("/private/**").authenticated() // Requires authentication for private URLs
//        .and()
//            .formLogin()
//                .loginPage("/login") // URL of the login page
//                .defaultSuccessUrl("/") // URL after successful login
//                .and()
//            .logout()
//                .logoutUrl("/logout") // URL to trigger logout
//                .logoutSuccessUrl("/") // URL after successful logout
//                .invalidateHttpSession(true) // Invalidate session
//                .deleteCookies("JSESSIONID"); // Delete cookies
//    }
//}
