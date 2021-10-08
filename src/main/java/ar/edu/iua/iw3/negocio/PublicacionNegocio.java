package ar.edu.iua.iw3.negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.*;

import ar.edu.iua.iw3.modelo.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.modelo.Publicacion;
import ar.edu.iua.iw3.modelo.persistencia.PublicacionRepository;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@Service
public class PublicacionNegocio implements IPublicacionNegocio {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PublicacionRepository publicacionDAO;

	@Autowired
	private UsuarioNegocio usuarioNegocio;

	@Override
	public List<Publicacion> listado() throws NegocioException {
		try {
			return publicacionDAO.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public Publicacion cargar(long id) throws NegocioException, NoEncontradoException {
		Optional<Publicacion> o;
		try {
			o = publicacionDAO.findById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No se encuentra la publicacion con el id=" + id);
		}
		return o.get();
	}

	@Override
	public Publicacion agregar(Publicacion publicacion) throws NegocioException, EncontradoException {
		try {
			cargar(publicacion.getId()); // tira excepcion sino no lo encuentra
			throw new EncontradoException("Ya existe una publicacion con id=" + publicacion.getId());
		} catch (NoEncontradoException e) {
		}
		try {
			return publicacionDAO.save(publicacion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public Publicacion modificar(Publicacion publicacion) throws NegocioException, NoEncontradoException {

		Optional<Publicacion> o;
		try {
			o = publicacionDAO.findById(publicacion.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No existe la publicacion a modificar");
		}
		try {
			return publicacionDAO.save(publicacion); // Como la publicacion existia, la actualizo
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public void eliminar(long id) throws NegocioException, NoEncontradoException {
		cargar(id);

		try {
			publicacionDAO.deleteById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public Publicacion listarPublicacionConMayorCantidadLikes() throws NegocioException, NoEncontradoException {
		Optional<Publicacion> o;
		try {
			o = publicacionDAO.findFirstByOrderByCantLikesDesc();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No hay publicaciones registradas");
		}
		return o.get();
	}

	@Override
	public List<Publicacion> listarPublicacionPorNombre(String nombre) throws NegocioException, NoEncontradoException {
		List<Publicacion> o = new ArrayList<>();
		try {
			o = publicacionDAO.findPublicacionByNombre(nombre);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (o.isEmpty()) {
			throw new NoEncontradoException("No hay publicaciones registradas con el nombre del usuario" + nombre);
		}
		return o;
	}

	@Override
	public List<Publicacion> findPublicacionDondeCantSuscriptoresMenorCantiLikes() throws NegocioException, NoEncontradoException {
		List<Publicacion> publicacionList = new ArrayList<>();
		List <Usuario> usuarioList = new ArrayList<>();
		List <Publicacion> aux = new ArrayList<>();
		try {
			publicacionList = publicacionDAO.findAll();
			usuarioList = usuarioNegocio.getUsuarioWhenPublicacionIsNotNull();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (publicacionList.isEmpty()|| usuarioList.isEmpty()) {
			throw new NoEncontradoException("No hay publicaciones registradas o no hay usuarios registrados");
		}
		//tengo todos usuarios que tienen publicaciones
		//
		for (Usuario u: usuarioList){
			long cantLikes = u.getIdPublicacion().getCantLikes();
			long cantSeguidores = u.getCantSeguidores();
			if (cantLikes>cantSeguidores)
				aux.add(u.getIdPublicacion());
		}

		return aux;
	}

	/*@Override
	public List<Publicacion> findPublicacionDondeCantSuscriptoresMenorCantiLikes()
			throws NegocioException, NoEncontradoException {
		List<Publicacion> o = new ArrayList<>();
		try {
			o = publicacionDAO.findPublicacionDondeCantSuscriptoresMenorCantiLikes();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (o.isEmpty()) {
			throw new NoEncontradoException("No hay publicaciones que tengan mas likes que suscriptores");
		}
		return o;
	}*/

	@Override
	public List<Publicacion> findPublicacionByFechaPublicacion(int hora)
			throws NegocioException, NoEncontradoException {
		
		List<Publicacion> o = new ArrayList<>();
		
		////////////////Armo la fecha /////////////////////////
		LocalDate localDate = null;
		ZoneId defaultZoneId = ZoneId.of("America/Argentina/Cordoba");
		localDate = LocalDate.now().minusDays(1);
		/////////////////////////////////////////////////////////////////
		
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

		
		date.setHours(hora);
		date.setMinutes(0);
		date.setSeconds(0);

		Date date1 = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		
		
		date1.setHours(hora);
		date1.setMinutes(59);
		date1.setSeconds(59);

		
		try {
			o = publicacionDAO.findPublicacionByFechaPublicacionBetween(date, date1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (o.isEmpty()) {
			throw new NoEncontradoException("No hay publicaciones que se hayan publicado ayer a la hora" + date);
		}
		return o;
	}

}
