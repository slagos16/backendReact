package BackendSekaiNoManga.SekainoMangaBase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  @Bean
  SecurityFilterChain security(HttpSecurity http) throws Exception {
    http.csrf(csrf->csrf.disable());
    http.authorizeHttpRequests(auth->auth
      .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/api/mangas/**").permitAll()
      .requestMatchers("/api/admin/**").hasRole("ADMIN")
      .anyRequest().authenticated());
    http.httpBasic();
    return http.build();
  }
}