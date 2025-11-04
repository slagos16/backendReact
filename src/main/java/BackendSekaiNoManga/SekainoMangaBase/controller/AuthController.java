
package BackendSekaiNoManga.SekainoMangaBase.controller;

import BackendSekaiNoManga.SekainoMangaBase.model.dto.UserSummaryDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;

    public AuthController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/me")
    public UserSummaryDTO me(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        String email = auth.getName();
        User u = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return new UserSummaryDTO(
                u.getId(),
                u.getEmail(),
                u.getNombre(),
                u.getRoles().stream()
                        // convertir SIEMPRE a String plano
                        .map(r -> r.getName().toString())
                        .collect(Collectors.toSet())
        );
    }

    @PostMapping("/login")
    public UserSummaryDTO login(Authentication auth) {
        return me(auth);
    }
}
