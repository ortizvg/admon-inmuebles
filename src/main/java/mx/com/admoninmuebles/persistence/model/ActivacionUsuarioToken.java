package mx.com.admoninmuebles.persistence.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "activacion_usuario_token")
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivacionUsuarioToken extends EntidadBase{
	
	private static final long serialVersionUID = 1L;

	private static final int EXPIRATION = 60 * 24;
	  
    @Id
    @Column(name = "id_usuario_activacion_usuario_token", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
  
    private String token;
    
    private boolean utilizado;
  
    @OneToOne(cascade = CascadeType.MERGE) @MapsId
    private Usuario usuario;
  
    private Date expiryDate;
    
    public ActivacionUsuarioToken() {
        super();
    }

    public ActivacionUsuarioToken(final String token) {
        super();

        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public ActivacionUsuarioToken(final String token, final Usuario usuario) {
        super();

        this.token = token;
        this.usuario = usuario;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

   

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    

}
