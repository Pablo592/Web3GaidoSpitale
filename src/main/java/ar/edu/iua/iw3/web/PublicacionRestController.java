package ar.edu.iua.iw3.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.iua.iw3.modelo.Publicacion;
import ar.edu.iua.iw3.negocio.IPublicacionNegocio;
import ar.edu.iua.iw3.negocio.PublicacionNegocio;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@RestController
public class PublicacionRestController {

	private Logger log = LoggerFactory.getLogger(PublicacionNegocio.class);

	@Autowired
	private IPublicacionNegocio publicacionNegocio;
	
	@GetMapping(value="/publicaciones")
	public ResponseEntity<List<Publicacion>> listado() {
			try {
				return new ResponseEntity<List<Publicacion>>(publicacionNegocio.listado(), HttpStatus.OK);
			} catch (NegocioException e) {
				return new ResponseEntity<List<Publicacion>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

	}

	@GetMapping(value="/publicaciones/publicacionPorNombreUsuario")
	public ResponseEntity<List<Publicacion>> listarPublicacionPorNombreUsuario(@RequestParam("nombre") String nombre) {
		try {
			return new ResponseEntity<List<Publicacion>>(publicacionNegocio.listarPublicacionPorNombre(nombre), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<Publicacion>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (NoEncontradoException e) {
			return new ResponseEntity<List<Publicacion>>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(value="/publicaciones/publicacionConMasLikes")
	public ResponseEntity<Publicacion> listarPublicacionConMasLikes() {
		try {
			return new ResponseEntity<Publicacion>(publicacionNegocio.listarPublicacionConMayorCantidadLikes(), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<Publicacion>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (NoEncontradoException e) {
			return new ResponseEntity<Publicacion>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(value="/publicaciones/mas-likes-que-suscriptores")
	public ResponseEntity<List<Publicacion>> listarPublicacionPorNombreUsuario() {
		try {
			return new ResponseEntity<List<Publicacion>>(publicacionNegocio.findPublicacionDondeCantSuscriptoresMenorCantiLikes(), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<Publicacion>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (NoEncontradoException e) {
			return new ResponseEntity<List<Publicacion>>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(value="/publicaciones/publicaciones-ayer")
	public ResponseEntity<List<Publicacion>> listarPublicacionesAyer(@RequestParam("hora") int hora) {
		try {
			return new ResponseEntity<List<Publicacion>>(publicacionNegocio.findPublicacionByFechaPublicacion(hora), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<Publicacion>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (NoEncontradoException e) {
			return new ResponseEntity<List<Publicacion>>(HttpStatus.NOT_FOUND);
		}

	}


	@PostMapping(value="/publicaciones")
	public ResponseEntity<String> agregar(@RequestBody Publicacion publicacion) {
		try {
			Publicacion respuesta=publicacionNegocio.agregar(publicacion);
			HttpHeaders responseHeaders=new HttpHeaders();
			responseHeaders.set("location", "/publicaciones/"+respuesta.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (EncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.FOUND);
		}
	}



	@GetMapping(value="/publicaciones/{id}")
	public ResponseEntity<Publicacion> cargar(@PathVariable("id") long id)  {
		try {
			return new ResponseEntity<Publicacion>(publicacionNegocio.cargar(id), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<Publicacion>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<Publicacion>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value="/publicaciones")
	public ResponseEntity<String> modificar(@RequestBody  Publicacion publicacion) {
		try {
			publicacionNegocio.modificar(publicacion);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(value="/publicaciones/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") long id)  {
		try {
			publicacionNegocio.eliminar(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
}
