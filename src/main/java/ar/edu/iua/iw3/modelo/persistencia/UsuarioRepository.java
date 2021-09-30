package ar.edu.iua.iw3.modelo.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.modelo.Publicacion;
import ar.edu.iua.iw3.modelo.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario,Long> {
	
	Optional<Usuario> findByEmail(String email);
	
	Optional<Usuario> findFirstByOrderByCantSeguidoresDesc();
	
	@Query(value = "select * from usuario e where year(e.fecha_nacimiento) = ? and month(e.fecha_nacimiento) = ?",nativeQuery = true)
	List<Usuario> getByYearAndMonth(int anio, int mes);

	List<Usuario> findByIdPublicacionTitulo(String titulo);
	
	
	
}
