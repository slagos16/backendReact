package BackendSekaiNoManga.SekainoMangaBase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.model.Manga.Estado;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {
  List<Manga> findByEliminadoFalseAndEstadoAndStockGreaterThan(Estado estado, int stock);
  List<Manga> findByEliminadoFalseAndEstadoAndStockGreaterThanAndPublisherContainingIgnoreCase(
      Estado estado, int stock, String publisher);
}