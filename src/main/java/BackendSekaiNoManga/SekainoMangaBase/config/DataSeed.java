package BackendSekaiNoManga.SekainoMangaBase.config;

import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import BackendSekaiNoManga.SekainoMangaBase.model.Role;
import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.repository.RoleRepository;
import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;

@Configuration
public class DataSeed {

  @Bean
  CommandLineRunner seed(RoleRepository roles, UserRepository users, PasswordEncoder pe) {
    return args -> {

      // Crea roles si no existen
      Role rAdmin = roles.findByName("ROLE_ADMIN")
          .orElseGet(() -> roles.save(Role.builder().name("ROLE_ADMIN").build()));

      Role rUser  = roles.findByName("ROLE_USER")
          .orElseGet(() -> roles.save(Role.builder().name("ROLE_USER").build()));

      // Crea admin si no existe
      users.findByEmail("admin@sekai.cl").orElseGet(() ->
          users.save(User.builder()
              .email("admin@sekai.cl")
              .passwordHash(pe.encode("admin123"))
              .roles(new HashSet<>(List.of(rAdmin, rUser)))
              .enabled(true)
              .build())
      );

      // Crea usuario normal si no existe
      users.findByEmail("user@sekai.cl").orElseGet(() ->
          users.save(User.builder()
              .email("user@sekai.cl")
              .passwordHash(pe.encode("user123"))
              .roles(new HashSet<>(List.of(rUser)))
              .enabled(true)
              .build())
      );
    };
  }
}