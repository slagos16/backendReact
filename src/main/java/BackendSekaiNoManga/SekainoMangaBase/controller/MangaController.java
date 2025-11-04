package BackendSekaiNoManga.SekainoMangaBase.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.service.MangaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController 
@RequiredArgsConstructor
@Tag(name="Mangas Público")
@RequestMapping("/api/mangas")
public class MangaController {
  private final MangaService service;

  @Operation(summary="Listado catálogo (solo activos con stock)")
  @GetMapping public List<Manga> all(@RequestParam(required=false) String publisher){
    return service.listarPublico(publisher);
  }

  @Operation(summary="Detalle por ID")
  @GetMapping("/{id}") public Manga byId(@PathVariable Long id){ return service.porId(id); }

  @Operation(summary="Reservar stock (carrito)")
  @PostMapping("/reservar-stock")
  public ResponseEntity<Void> reservar(@RequestBody Map<Long,Integer> items){
    service.reservarStock(items);
    return ResponseEntity.ok().build();
  }
}

@RestController @RequiredArgsConstructor
@Tag(name="Mangas Admin")
@RequestMapping("/api/admin/mangas")
class MangaAdminController {
  private final MangaService service;

  @PostMapping public Manga create(@Valid @RequestBody Manga m){ return service.crear(m); }
  @PutMapping("/{id}") public Manga update(@PathVariable Long id, @Valid @RequestBody Manga m){ return service.actualizar(id,m); }
  @PatchMapping("/{id}/stock") public Manga patchStock(@PathVariable Long id, @RequestBody Map<String,Integer> body){ return service.patchStock(id, body.get("stock")); }
  @PatchMapping("/{id}/estado") public Manga patchEstado(@PathVariable Long id, @RequestBody Map<String,String> body){
    return service.patchEstado(id, Manga.Estado.valueOf(body.get("estado")));
  }
  @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){ service.softDelete(id); return ResponseEntity.noContent().build(); }
}