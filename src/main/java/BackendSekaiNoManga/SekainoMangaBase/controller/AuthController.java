package BackendSekaiNoManga.SekainoMangaBase.controller;

import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.RegisterDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.ChangePasswordDTO;
import BackendSekaiNoManga.SekainoMangaBase.service.AuthService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public User register(@Valid @RequestBody RegisterDTO dto) {
    return authService.register(dto);
  }

  @PostMapping("/change-password")
  public void changePassword(Authentication auth, @Valid @RequestBody ChangePasswordDTO dto) {
    authService.changePassword(auth, dto);
  }

  // Handy: ver identity actual
  @GetMapping("/me")
  public PrincipalDTO me(Authentication auth) {
    PrincipalDTO dto = new PrincipalDTO();
    dto.setUsername(auth != null ? auth.getName() : null);
    return dto;
  }

  @Data
  static class PrincipalDTO {
    private String username;
  }
}
