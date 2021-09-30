package ar.edu.iua.iw3.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.iua.iw3.modelo.Usuario;
import ar.edu.iua.iw3.modelo.persistencia.UsuarioRepository;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

@Service
public class UsuarioNegocio  implements IUsuarioNegocio{

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UsuarioRepository usuarioDAO;
	
	
	@Override
	public List<Usuario> listado() throws NegocioException {
		try {
			return usuarioDAO.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public Usuario cargar(long id) throws NegocioException, NoEncontradoException {
		
		Optional<Usuario> o;
		try {
			o = usuarioDAO.findById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No se encuentra el usuario con el id=" + id);
		}
		return o.get();
	}

	@Override
	public Usuario agregar(Usuario usuario) throws NegocioException, EncontradoException {
		try {
			if(null!=findUserByEmail(usuario.getEmail()))
				throw new EncontradoException("Ya existe un usuario con el email  =" + usuario.getEmail());
			cargar(usuario.getId());  	// tira excepcion sino no lo encuentra
			throw new EncontradoException("Ya existe un usuario con id=" + usuario.getId());
		} catch (NoEncontradoException e) {
		}
		try {
			return usuarioDAO.save(usuario);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	private Usuario findUserByEmail(String email) {
		Optional <Usuario>  o  = usuarioDAO.findByEmail(email);
		if(o.isPresent())
			return o.get();
		else
			return null;
	}
	
	
	@Override
	public Usuario modificar(Usuario usuario) throws NegocioException, NoEncontradoException {
				//me viene un usuario con su id
				//Paso 1: busco existencia del usuario con ese id
				//Paso 2: busco existencia de usuario que ya tienen asignado ese email
				//Paso 3_a: si el "email" del usuario esta asignado a un usuario con diferente id del que se quiere modificar entonces tengo que generar un error
				//Paso 3_b: si el "id" del usuario es el mismo id del usuario que me viene, entonces no se debe de generar error
				//Paso 4:  Sino ningun usuario  tiene asignado el "email" se lo debe de modificar sin problemas

				cargar(usuario.getId()); //Paso 1
				Usuario usuarioExist = findUserByEmail(usuario.getEmail());

				if(null != usuarioExist ) { //Paso 2

					if (usuario.getId() != usuarioExist.getId())
						throw new NegocioException("Ya existe un usuario con el id" + usuarioExist.getId() + " con email  ="
								+ usuario.getEmail() );	//Paso 3_a

					return	saveUsuario(usuario);	//Paso 3_b
				}

				return saveUsuario(usuario);	//Paso 4
	}
	
	private Usuario saveUsuario(Usuario usuario) throws NegocioException {
		try {
			return usuarioDAO.save(usuario); // sino existe el usuario lo cargo
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
	}

	@Override
	public void eliminar(long id) throws NegocioException, NoEncontradoException {
		cargar(id);
		try {
			usuarioDAO.deleteById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		
	}
	
	@Override
	public Usuario findPerfilConMasSeguidores() throws NegocioException, NoEncontradoException {
		Optional<Usuario> o;
		try {
			o = usuarioDAO.findFirstByOrderByCantSeguidoresDesc();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No hay perfiles registrados" );
		}
		return o.get();
	}
	
	
	@Override
	public List<Usuario> findPerfilPorAnioYMes(int anio, int mes) throws NegocioException, NoEncontradoException {
		List <Usuario> o = new ArrayList<>();
		try {
			o = usuarioDAO.getByYearAndMonth(anio,mes);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (o.isEmpty()) {
			throw new NoEncontradoException("No hay perfiles registrados con esa fecha indicada" );
		}
		return o;
		
	}


	@Override
	public List<Usuario> findUsuarioByTituloPublicacion(String tituloNombre) throws NegocioException, NoEncontradoException {
		List <Usuario> o = new ArrayList<>();
		try {
			o = usuarioDAO.findByIdPublicacionTitulo(tituloNombre);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (o.isEmpty()) {
			throw new NoEncontradoException("No hay usuario que tenga una publicacion con titulo primavera" );
		}
		return o;
	}

	@Override
	public Usuario findUsuarioPorCorreo (String email) throws NegocioException, NoEncontradoException {
		Optional<Usuario> o;
		try {
			o = usuarioDAO.findByEmail(email);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NegocioException(e);
		}
		if (!o.isPresent()) {
			throw new NoEncontradoException("No hay usuarios registrados con el email"+ email );
		}
		return o.get();
	}


}
