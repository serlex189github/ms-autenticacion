package co.com.pragma.autenticacion.model.usuario.gateways;

import co.com.pragma.autenticacion.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UsuarioRepository {


    /** Verifica si existe un usuario*/
    Mono<Boolean> existsByEmail(String correo);

    /** Persiste un nuevo usuario y devuelve el agregado con id asignado. */
    Mono<Usuario> save(Usuario usuario);

    Flux<Usuario> findAll();
    /**
     * Actualiza un usuario existente (id requerido). Si no existe debe emitir error.
     */
    Mono<Usuario> update(Usuario usuario);

    /** Obtiene un usuario por su id. */
    Mono<Usuario> findById(Integer  id);

    /** Obtiene un usuario por correo (case-insensitive). */
    Mono<Usuario> findByCorreo(String correo);

    /** Elimina por id (completa incluso si el id no existe). */
    Mono<Void> deleteById(Integer  id);

}
