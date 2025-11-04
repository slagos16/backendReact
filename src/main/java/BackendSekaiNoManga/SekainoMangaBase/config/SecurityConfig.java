package BackendSekaiNoManga.SekainoMangaBase.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService customUserDetailsService; // tu CustomUserDetailsService

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // <- Bean requerido por DataSeed y AuthProvider
  }

  @Bean
  public DaoAuthenticationProvider daoAuthProvider() {
    DaoAuthenticationProvider p = new DaoAuthenticationProvider();
    p.setUserDetailsService(customUserDetailsService);
    p.setPasswordEncoder(passwordEncoder());
    return p;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .authenticationProvider(daoAuthProvider())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, "/api/mangas/**").permitAll() // catálogo público
        .requestMatchers("/api/admin/**").hasRole("ADMIN")              // zona admin
        .requestMatchers("/api/auth/**").authenticated()
        .anyRequest().permitAll()
      )
      .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins("http://localhost:3000", "http://localhost:5173")
          .allowedMethods("*")
          .allowCredentials(true);
      }
    };
  }
}
