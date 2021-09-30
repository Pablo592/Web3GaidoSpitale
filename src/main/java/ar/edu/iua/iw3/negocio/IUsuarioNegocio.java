package ar.edu.iua.iw3.negocio;

import java.util.List;

import ar.edu.iua.iw3.modelo.Publicacion;
import ar.edu.iua.iw3.modelo.Usuario;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

public interface IUsuarioNegocio {
	
	public List<Usuario> listado() throws NegocioException;

	public Usuario cargar(long id) throws NegocioException, NoEncontradoException;

	public Usuario agregar(Usuario usuario) throws NegocioException, EncontradoException;

	public Usuario modificar(Usuario usuario) throws NegocioException, NoEncontradoException;

	public void eliminar(long id) throws NegocioException, NoEncontradoException;

	public Usuario findPerfilConMasSeguidores() throws NegocioException, NoEncontradoException;

	public List<Usuario> findPerfilPorAnioYMes(int anio, int mes) throws NegocioException, NoEncontradoException;

	public List<Usuario> findUsuarioByTituloPublicacion(String tituloNombre)throws NegocioException, NoEncontradoException;

	public Usuario findUsuarioPorCorreo(String Email) throws NegocioException, NoEncontradoException;

}
