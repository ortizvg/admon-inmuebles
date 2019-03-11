package mx.com.admoninmuebles.dto;

import lombok.Data;

@Data
public class ReservacionDto {
    private Long id;
    private String title;

//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @DateTimeFormat(iso = ISO.DATE)
//    private LocalDate start;
//
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @DateTimeFormat(iso = ISO.DATE)
//    private LocalDate end;
    
    private String start;
    private String end;

    private Long areaComunId;
    private String areaComunNombre;

    private Long socioId;
    private String socioNombre;
    
    private Long pagoId;
    private String pagoEstatusPagoName;
    private String pagoEstatusPagoDescripction;
    
}