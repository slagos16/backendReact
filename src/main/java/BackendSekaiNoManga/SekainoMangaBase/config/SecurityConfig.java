package BackendSekaiNoManga.SekainoMangaBase.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService uds;   // ← inyectamos tu servicio

  @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  @Bean
  DaoAuthenticationProvider authProvider() {
    var p = new DaoAuthenticationProvider();
    p.setUserDetailsService(uds);
    p.setPasswordEncoder(passwordEncoder());
    return p;
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain security(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());
    http.headers(h -> h.frameOptions(f -> f.disable())); // H2 console
    http.authenticationProvider(authProvider());
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/h2-console/**","/api/mangas/**").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
    );
    http.httpBasic(); // Basic Auth
    return http.build();
  }
}
