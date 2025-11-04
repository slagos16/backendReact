package BackendSekaiNoManga.SekainoMangaBase.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Table(name = "Manga")
@NoArgsConstructor
@AllArgsConstructor
public class Manga {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mangaId; 
    
    @Column(length = 50, nullable = false)
    private String mangaName;  
    
    @Column(length = 50, nullable = false)
    private Integer price;  

    @Column(length = 50, nullable = false)
    private String publisher;  
    
}
