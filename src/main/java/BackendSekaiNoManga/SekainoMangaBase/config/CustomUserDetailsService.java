package BackendSekaiNoManga.SekainoMangaBase.config;

import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository users;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var u = users.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("No existe: " + email));

    var authorities = u.getRoles().stream()
        .map(r -> new SimpleGrantedAuthority(r.getName())) // "ROLE_ADMIN", "ROLE_USER"
        .toList();

    return org.springframework.security.core.userdetails.User
        .withUsername(u.getEmail())           // ← usaremos email como username
        .password(u.getPasswordHash())        // ← BCrypt desde tu seed
        .authorities(authorities)
        .accountLocked(!u.isEnabled())
        .build();
  }
}