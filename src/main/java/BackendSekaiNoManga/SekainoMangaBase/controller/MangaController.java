package BackendSekaiNoManga.SekainoMangaBase.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.service.MangaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/v1/manga")
@Tag(name = "Mangas", description = "Operaciones para administrar Mangas")

public class MangaController {
       @Autowired
    private MangaService mangaService;

    @GetMapping()
    @Operation(summary = "Listar todos los Mangas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Manga encontrados"),
        @ApiResponse(responseCode = "204", description = "No hay Mangaes registrados")
    })
    public ResponseEntity<List<Manga>> listMangaes() {
        List<Manga> prods = mangaService.findAll();
        if (prods.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prods);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un Manga por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Manga encontrado"),
        @ApiResponse(responseCode = "404", description = "Manga no encontrado")
    })
    public ResponseEntity<Manga> getMangaById(
        @Parameter(description = "ID del Manga a buscar") @PathVariable Integer id) {
        try {
            Manga p = mangaService.findById(id);
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping()
    @Operation(summary = "Crear un nuevo Manga")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Manga creado exitosamente")
    })
    public ResponseEntity<Manga> createManga(@RequestBody Manga manga) {
        Manga created = mangaService.save(manga);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un Manga existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Manga actualizado"),
        @ApiResponse(responseCode = "404", description = "Manga no encontrado")
    })
    public ResponseEntity<Manga> updateManga(
        @Parameter(description = "ID del Manga a actualizar") @PathVariable Integer id,
        @RequestBody Manga manga) {
        try {
            Manga updated = mangaService.updateManga(id, manga);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un Manga")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Manga eliminado"),
        @ApiResponse(responseCode = "404", description = "Manga no encontrado")
    })
    public ResponseEntity<Void> deleteManga(
        @Parameter(description = "ID del Manga a eliminar") @PathVariable Integer id) {
        try {
            mangaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}