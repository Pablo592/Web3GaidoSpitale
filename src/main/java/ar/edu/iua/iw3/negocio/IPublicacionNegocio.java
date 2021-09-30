package ar.edu.iua.iw3.negocio;

import java.util.Date;
import java.util.List;
import ar.edu.iua.iw3.modelo.Publicacion;
import ar.edu.iua.iw3.negocio.excepciones.EncontradoException;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;

public interface IPublicacionNegocio {
	
	public List<Publicacion> listado() throws NegocioException;

	public Publicacion cargar(long id) throws NegocioException, NoEncontradoException;

	public Publicacion agregar(Publicacion publicacion) throws NegocioException, EncontradoException;

	public Publicacion modificar(Publicacion publicacion) throws NegocioException, NoEncontradoException;

	public void eliminar(long id) throws NegocioException, NoEncontradoException;

	public Publicacion listarPublicacionConMayorCantidadLikes() throws NegocioException, NoEncontradoException;

	public List<Publicacion> listarPublicacionPorNombre(String name) throws NegocioException, NoEncontradoException;

	public List<Publicacion> findPublicacionDondeCantSuscriptoresMenorCantiLikes () throws NegocioException, NoEncontradoException;

	public List<Publicacion> findPublicacionByFechaPublicacion(int hora) throws NegocioException, NoEncontradoException;
}
