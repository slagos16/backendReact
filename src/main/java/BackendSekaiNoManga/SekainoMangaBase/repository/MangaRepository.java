package BackendSekaiNoManga.SekainoMangaBase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Integer> {

    

    
}
