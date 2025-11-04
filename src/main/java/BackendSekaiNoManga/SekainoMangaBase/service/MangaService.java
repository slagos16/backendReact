package BackendSekaiNoManga.SekainoMangaBase.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.model.Manga.Estado;
import BackendSekaiNoManga.SekainoMangaBase.repository.MangaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service 
@RequiredArgsConstructor

public class MangaService {
  private final MangaRepository repo;

  public List<Manga> listarPublico(String publisher){
    return (publisher==null || publisher.isBlank())
      ? repo.findByEliminadoFalseAndEstadoAndStockGreaterThan(Estado.ACTIVO, 0)
      : repo.findByEliminadoFalseAndEstadoAndStockGreaterThanAndPublisherContainingIgnoreCase(
          Estado.ACTIVO, 0, publisher);
  }

  public List<Manga> listarAdmin(){ return repo.findAll(); }

  public Manga porId(Long id){
    return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Manga no encontrado"));
  }

  public Manga crear(Manga m){ return repo.save(m); }

  public Manga actualizar(Long id, Manga in){
    Manga db = porId(id);
    db.setMangaName(in.getMangaName());
    db.setPrice(in.getPrice());
    db.setPublisher(in.getPublisher());
    db.setPortadaUrl(in.getPortadaUrl());
    db.setStock(in.getStock());
    db.setEstado(in.getEstado());
    return repo.save(db);
  }

  public void softDelete(Long id){
    Manga db = porId(id); db.setEliminado(true); repo.save(db);
  }

  public Manga patchStock(Long id, Integer stock){
    if (stock==null || stock<0) throw new IllegalArgumentException("Stock inválido");
    Manga db = porId(id); db.setStock(stock); return repo.save(db);
  }

  public Manga patchEstado(Long id, Manga.Estado estado){
    Manga db = porId(id); db.setEstado(estado); return repo.save(db);
  }

  /** items: { mangaId -> qty } */
  @Transactional
  public void reservarStock(Map<Long,Integer> items){
    for (var e: items.entrySet()){
      Manga m = porId(e.getKey());
      int qty = Optional.ofNullable(e.getValue()).orElse(0);
      if (qty<=0 || m.getStock()<qty) throw new IllegalArgumentException("Stock insuficiente");
      m.setStock(m.getStock()-qty);
    }
  }
}