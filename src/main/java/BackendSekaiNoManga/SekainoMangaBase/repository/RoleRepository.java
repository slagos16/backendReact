package BackendSekaiNoManga.SekainoMangaBase.repository;

import java.util.Optional;
import BackendSekaiNoManga.SekainoMangaBase.model.Role; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}