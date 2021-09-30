package ar.edu.iua.iw3.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.modelo.Publicacion;
import ar.edu.iua.iw3.modelo.Usuario;
import ar.edu.iua.iw3.negocio.IUsuarioNegocio;
import ar.edu.iua.iw3.negocio.UsuarioNegocio;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@RestController
public class UsuarioRestController {

	private Logger log = LoggerFactory.getLogger(UsuarioNegocio.class);
	
	@Autowired
	private IUsuarioNegocio usuarioNegocio;
	
	@GetMapping(value="/usuarios")
	public ResponseEntity<List<Usuario>> listado() throws NegocioException {
			try {
				return new ResponseEntity<List<Usuario>>(usuarioNegocio.listado(), HttpStatus.OK);
			} catch (NegocioException e) {
				return new ResponseEntity<List<Usuario>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	}
	
	
	@GetMapping(value="/usuarios/mayor_seguidores")
	public ResponseEntity<Usuario> listadoUsuarioConMayorSeguidores()  {
			try {
				return new ResponseEntity<Usuario>(usuarioNegocio.findPerfilConMasSeguidores(), HttpStatus.OK);
			} catch (NegocioException e) {
				return new ResponseEntity<Usuario>(HttpStatus.INTERNAL_SERVER_ERROR);
			}catch (NoEncontradoException e) {
				return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
			}

	}
	
	@GetMapping(value="/usuarios/buscarPorFechaNacimiento")
	public ResponseEntity<List<Usuario>> listadoUsuarioPorFechaNacimiento(@RequestParam ("mes") int mes, @RequestParam ("anio")int anio)  {
			try {
				return new ResponseEntity<List<Usuario>>(usuarioNegocio.findPerfilPorAnioYMes(anio, mes), HttpStatus.OK);
			} catch (NegocioException e) {
				return new ResponseEntity<List<Usuario>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}catch (NoEncontradoException e) {
				return new ResponseEntity<List<Usuario>>(HttpStatus.NOT_FOUND);
			}

	}


	@GetMapping(value="/usuarios/buscarUsuariosPorTituloPublicacion")
	public ResponseEntity<List<Usuario>> listadoUsuariosPorPublicacionTitulo(@RequestParam ("nombre") String nombre)  {
		try {
			return new ResponseEntity<List<Usuario>>(usuarioNegocio.findUsuarioByTituloPublicacion(nombre), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<List<Usuario>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (NoEncontradoException e) {
			return new ResponseEntity<List<Usuario>>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(value="/usuarios/buscarPorEmail")
	public ResponseEntity<Usuario> listadoUsuarioPorEmail(@RequestParam ("email") String email)  {
		try {
			return new ResponseEntity<Usuario>(usuarioNegocio.findUsuarioPorCorreo(email), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<Usuario>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (NoEncontradoException e) {
			return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
		}

	}
	
	@PostMapping(value="/usuarios")
	public ResponseEntity<String> agregar(@RequestBody Usuario usuario) {
		try {
			Usuario respuesta=usuarioNegocio.agregar(usuario);
			HttpHeaders responseHeaders=new HttpHeaders();
			responseHeaders.set("location", "/usuarios/"+respuesta.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (EncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.FOUND);
		}
	}

	
	@GetMapping(value="/usuarios/{id}")
	public ResponseEntity<Usuario> cargar(@PathVariable("id") long id)  {
		try {
			return new ResponseEntity<Usuario>(usuarioNegocio.cargar(id), HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<Usuario>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value="/usuarios")
	public ResponseEntity<String> modificar(@RequestBody  Usuario usuario) {
		try {
			usuarioNegocio.modificar(usuario);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(value="/usuarios/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") long id) {
		try {
			usuarioNegocio.eliminar(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NegocioException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoEncontradoException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
}
