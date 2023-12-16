package com.sportevents.config;

import com.sportevents.config.jwt.FirebaseTokenProvider;
import com.sportevents.config.jwt.JwtConfigurer;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableAsync
public class SecurityConfig {


    @Autowired
    private FirebaseTokenProvider firebaseTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.apply(new JwtConfigurer(firebaseTokenProvider));
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/admin").hasAuthority("ADMIN")
                        .requestMatchers("/api/user/all").hasAuthority("ADMIN")
                        .requestMatchers("/api/auth/register").permitAll()
                        .anyRequest().authenticated()
        );
        http.csrf().disable().cors();

        return http.build();
    }

    //disable CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://frontend:3000")); // replace with your url
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
