package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ServicioDto {

    private Long id;

    @NotNull
    @Size(min = 5, max = 100)
    private String titulo;

    @NotNull
    @Size(min = 10, max = 2000)
    private String descripcion;

    @NotNull
    private MultipartFile imagen;

    private String imagenUrl;

}
