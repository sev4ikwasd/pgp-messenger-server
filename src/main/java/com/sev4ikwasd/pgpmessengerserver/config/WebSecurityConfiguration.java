package com.sev4ikwasd.pgpmessengerserver.config;

import com.sev4ikwasd.pgpmessengerserver.config.service.RestAuthenticationEntryPoint;
import com.sev4ikwasd.pgpmessengerserver.config.service.jwt.JwtAuthenticationProvider;
import com.sev4ikwasd.pgpmessengerserver.config.service.jwt.JwtTokenAuthenticationProcessingFilter;
import com.sev4ikwasd.pgpmessengerserver.config.service.jwt.SkipPathRequestMatcher;
import com.sev4ikwasd.pgpmessengerserver.config.service.jwt.extractor.TokenExtractor;
import com.sev4ikwasd.pgpmessengerserver.config.service.login.LoginAuthenticationProvider;
import com.sev4ikwasd.pgpmessengerserver.config.service.login.LoginProcessingFilter;
import com.sev4ikwasd.pgpmessengerserver.config.settings.WebSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private WebSettings webSettings;
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private LoginAuthenticationProvider loginAuthenticationProvider;
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    private TokenExtractor tokenExtractor;
    private AuthenticationManager authenticationManager;

    @Autowired

    public WebSecurityConfiguration(WebSettings webSettings, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, RestAuthenticationEntryPoint authenticationEntryPoint, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, LoginAuthenticationProvider loginAuthenticationProvider, JwtAuthenticationProvider jwtAuthenticationProvider, TokenExtractor tokenExtractor, @Lazy AuthenticationManager authenticationManager) {
        this.webSettings = webSettings;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.loginAuthenticationProvider = loginAuthenticationProvider;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.tokenExtractor = tokenExtractor;
        this.authenticationManager = authenticationManager;
    }

    protected LoginProcessingFilter buildLoginProcessingFilter(String loginEntryPoint) throws Exception {
        LoginProcessingFilter filter = new LoginProcessingFilter(loginEntryPoint, successHandler, failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter
                = new JwtTokenAuthenticationProcessingFilter(webSettings, failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(webSettings.getAuthenticationUrl(), webSettings.getRefreshTokenUrl(), webSettings.getRegisterUrl());
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(webSettings.getApiRootUrl()).authenticated()
                .and()
                .addFilterBefore(buildLoginProcessingFilter(webSettings.getAuthenticationUrl()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, webSettings.getApiRootUrl()), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuthenticationProvider).authenticationProvider(jwtAuthenticationProvider).userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}