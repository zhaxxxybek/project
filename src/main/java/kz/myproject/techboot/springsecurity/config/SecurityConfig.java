package kz.myproject.techboot.springsecurity.config;

import kz.myproject.techboot.springsecurity.Service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService(){
        return new UserService();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService()).passwordEncoder(passwordEncoder());

        http
                .formLogin(loginConfigurer ->
                        loginConfigurer
                                .loginPage("/sign-in-page")
                                .loginProcessingUrl("/auth")
                                .usernameParameter("user_email")
                                .passwordParameter("user_password")
                                .defaultSuccessUrl("/profile")
                                .failureUrl("/sign-in-page?autherror")
                );
        http
                .logout(logoutConfigurer ->
                        logoutConfigurer
                                .logoutUrl("/sign-out")
                                .logoutSuccessUrl("/sign-in-page")
                );

        http.csrf().disable();
        return http.build();
    }



}
