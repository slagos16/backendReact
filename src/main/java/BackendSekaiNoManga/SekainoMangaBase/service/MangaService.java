package BackendSekaiNoManga.SekainoMangaBase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.repository.MangaRepository;
@Service
public class MangaService {
      @Autowired
    private final MangaRepository mangaRepository;

        public MangaService(MangaRepository mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public List<Manga> findAll() {
        return mangaRepository.findAll();
    }

    public Manga findById(Integer id) {
        return mangaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Manga no encontrado"));
    }

    public Manga save(Manga Manga) {
        return mangaRepository.save(Manga);
    }

    public void delete(Integer id) {
    Manga Manga = mangaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Manga no encontrado"));
    mangaRepository.delete(Manga);
    }

    public Manga updateManga(Integer id, Manga newData) {
    return MangaRepository.findById(id)
    .map(existing -> {
    existing.setMangaName(null);(newData.getMangaName());
    existing.setPrice(id);(newData.getPrice());
    existing.setPublisher(null);(newData.getPublisher());
    return MangaRepository.save(existing);
})
.orElseThrow(() -> new RuntimeException("Manga no encontrado"));

}
}

