package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "reservaciones")
@Data
@EqualsAndHashCode(callSuper = false)
public class Reservacion extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservacion")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String title;

//    private LocalDate start;
//
//    private LocalDate end;

    private String start;
    private String end;

    
    @ManyToOne
    @JoinColumn(name = "id_area_comun_fk")
    private AreaComun areaComun;

    @ManyToOne
    @JoinColumn(name = "id_socio_fk")
    private Usuario socio;
    
//    @OneToOne(orphanRemoval = true)
//    @OneToOne(cascade = CascadeType.REMOVE)
    @OneToOne
    @JoinColumn(name = "id_pago_fk")
    private Pago pago;

}