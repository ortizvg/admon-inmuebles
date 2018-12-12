package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ProveedorDto;

public interface ProveedorService {
	
	Collection<ProveedorDto> getProveedores();
	ProveedorDto buscarProveedorPorId(Long idProveedor);
	ProveedorDto guardar(ProveedorDto proveedorDto);
	ProveedorDto editar(ProveedorDto proveedorDto);
	void eliminar(Long idProveedor);
	boolean exist(final Long id);
	Collection<ProveedorDto>  buscarProveedorPorZona(String codigoZona);

}
