package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.SucursalDto;
import mx.com.admoninmuebles.persistence.model.Sucursal;

public interface SucursalService {
	SucursalDto save(SucursalDto sucursalDto);

    Collection<SucursalDto> findAll();

    SucursalDto findById(Long idSucursal);

    void deleteById(Long idSucursal);
}
