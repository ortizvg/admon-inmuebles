package mx.com.admoninmuebles.rest.resource;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ReservacionDto;
import mx.com.admoninmuebles.service.ReservacionService;

@RestController
@RequestMapping("/api")
public class ReservacionResource {
	
    @Autowired
    private ReservacionService reservacioService;
	
	@GetMapping("/reservaciones")
	public ResponseEntity<Collection<ReservacionDto>> buscarReservaciones(@RequestParam(name = "areaComunId", required = false) Long areaComunId , @RequestParam(name = "socioId", required = false) Long socioId, final HttpServletRequest request, final HttpSession session) {
		try {
			Collection<ReservacionDto> reservaciones =  Collections.emptyList();
			
			if (null != areaComunId ) {
				reservaciones =  reservacioService.findByAreaComun( areaComunId );
			} else if ( request.isUserInRole( RolConst.ROLE_SOCIO_BI ) ) {
				reservaciones =  reservacioService.findBySocio( socioId ) ;
			}
			session.setAttribute("areaComunId", areaComunId);
			
			return new ResponseEntity<>(reservaciones, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
		
		
	}

}
