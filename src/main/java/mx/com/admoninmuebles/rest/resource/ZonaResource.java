package mx.com.admoninmuebles.rest.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.ZonaService;

@RestController
@RequestMapping("/api")
public class ZonaResource {

	@Autowired
	private ZonaService zonaService;

	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
	@GetMapping("/zonas")
	public ResponseEntity<Collection<ZonaDto>> buscarZonas(final HttpServletRequest request) {
		Optional<Long> optId = SecurityUtils.getCurrentUserId();
		try {
			Collection<ZonaDto> zonas = Collections.emptyList();
			if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
				zonas = zonaService.findAll();
			} else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
				zonas = zonaService.findByAdminZonaId(optId.get());
			} else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
				zonas = zonaService.findByAdministradoresBiId(optId.get());
			}
			return new ResponseEntity<>(zonas, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}

	}

}
