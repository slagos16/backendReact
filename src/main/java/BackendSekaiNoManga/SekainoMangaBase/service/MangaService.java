package BackendSekaiNoManga.SekainoMangaBase.service;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.model.Manga.Estado;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.CartRequest;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.MangaCreateDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.MangaUpdateDTO;
import BackendSekaiNoManga.SekainoMangaBase.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MangaService {

  private final MangaRepository repo;

  /* ===== CRUD ADMIN ===== */

  @Transactional
  public Manga create(MangaCreateDTO dto) {
    Manga m = new Manga();
    m.setMangaName(dto.getMangaName());
    m.setPrice(dto.getPrice());
    m.setPublisher(dto.getPublisher());
    m.setStock(dto.getStock());
    m.setPortadaUrl(dto.getPortadaUrl());
    m.setEstado(Estado.ACTIVO);
    m.setEliminado(false);
    return repo.save(m);
  }

  @Transactional
  public Manga update(Long id, MangaUpdateDTO dto) {
    Manga m = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga no existe: " + id));

    if (dto.getMangaName() != null) m.setMangaName(dto.getMangaName());
    if (dto.getPrice() != null)      m.setPrice(dto.getPrice());
    if (dto.getPublisher() != null)  m.setPublisher(dto.getPublisher());
    if (dto.getStock() != null)      m.setStock(dto.getStock());
    if (dto.getPortadaUrl() != null) m.setPortadaUrl(dto.getPortadaUrl());
    if (dto.getEstado() != null)     m.setEstado(dto.getEstado());

    return repo.save(m);
  }

  @Transactional
  public Manga updateStock(Long id, int newStock) {
    Manga m = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga no existe: " + id));
    if (newStock < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stock inválido");
    m.setStock(newStock);
    return repo.save(m);
  }

  @Transactional
  public void delete(Long id) {
    Manga m = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga no existe: " + id));
    m.setEliminado(true); // borrado lógico
    repo.save(m);
  }

  /* ===== Carrito / Stock ===== */

  @Transactional
  public void reservarStock(CartRequest cart) {
    for (var it : cart.getItems()) {
      int updated = repo.tryDecrementStock(it.getMangaId(), it.getQty());
      if (updated == 0) {
        // si cualquiera falla, se hace rollback de toda la transacción
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Sin stock suficiente para mangaId=" + it.getMangaId()
        );
      }
    }
  }

  @Transactional
  public void liberarStock(CartRequest cart) {
    for (var it : cart.getItems()) {
      repo.incrementStock(it.getMangaId(), it.getQty());
    }
  }

  @Transactional
  public void checkout(CartRequest cart) {
    // por ahora es igual a reservar; más adelante crea Orden/Pago
    reservarStock(cart);
  }
}
