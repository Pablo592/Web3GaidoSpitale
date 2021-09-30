package ar.edu.iua.iw3.modelo.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ar.edu.iua.iw3.modelo.Publicacion;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends JpaRepository <Publicacion,Long> {
    Optional<Publicacion> findFirstByOrderByCantLikesDesc();

    @Query(value = "select p.id, p.cant_likes,p.descripcion,p.fecha_publicacion,p.titulo from usuario as e "
            + "	inner join publicacion p on p.id = e.id_publicacion"
            + " where e.nombre = ? ",nativeQuery = true)
    List<Publicacion> findPublicacionByNombre(String nombre);


    @Query(value = "select p.id, p.cant_likes,p.descripcion,p.fecha_publicacion,p.titulo from usuario as e" +
                        " inner join publicacion p on p.id = e.id_publicacion" +
                        " where e.cant_seguidores < p.cant_likes",nativeQuery = true)
    List<Publicacion> findPublicacionDondeCantSuscriptoresMenorCantiLikes();

    List<Publicacion> findPublicacionByFechaPublicacionBetween(Date date1, Date date2);


}
