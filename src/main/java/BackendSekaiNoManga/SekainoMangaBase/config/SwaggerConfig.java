package BackendSekaiNoManga.SekainoMangaBase.config;

import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

public class SwaggerConfig {
          @Bean 
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API 2025 prueba 3 FULLSTACK - SekaiNoManga")
                        .version("1.0")
                        .description("Documentación de la API para el sistema de Gestion de la pagina SekaiNoManga "));
    }
}
    
