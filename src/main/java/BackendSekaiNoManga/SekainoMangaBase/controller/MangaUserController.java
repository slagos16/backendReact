package BackendSekaiNoManga.SekainoMangaBase.controller;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.CartRequest;
import BackendSekaiNoManga.SekainoMangaBase.repository.MangaRepository;
import BackendSekaiNoManga.SekainoMangaBase.service.MangaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mangas")
@RequiredArgsConstructor
public class MangaUserController {

  private final MangaRepository repo;
  private final MangaService service;

  // Catálogo público (solo activos, no eliminados y con stock)
  @GetMapping
  public List<Manga> list() {
    return repo.findByEliminadoFalseAndEstadoAndStockGreaterThan(Manga.Estado.ACTIVO, 0);
  }

  @GetMapping("/{id}")
  public Manga byId(@PathVariable Long id) {
    return repo.findById(id).orElseThrow();
  }

  // reservar stock (carrito)
  @PostMapping("/reservar-stock")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void reservar(@Valid @RequestBody CartRequest cart) {
    service.reservarStock(cart);
  }

  // liberar reserva (si el usuario cancela)
  @PostMapping("/liberar-stock")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void liberar(@Valid @RequestBody CartRequest cart) {
    service.liberarStock(cart);
  }

  // checkout directo (si no manejas reservas separadas)
  @PostMapping("/checkout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void checkout(@Valid @RequestBody CartRequest cart) {
    service.checkout(cart);
  }
}
