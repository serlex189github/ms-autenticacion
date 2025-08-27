package co.com.pragma.autenticacion.usecase.usuario;

import co.com.pragma.autenticacion.model.usuario.Usuario;
import co.com.pragma.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private  final UsuarioRepository usuarioRepository;


//    public Mono<Usuario> saveUsuario(Usuario usuario) {
//        return usuarioRepository.save(usuario);
//    }


    public Mono<Usuario> saveUsuario(Usuario usuario) {
        // Validaciones mínimas (tu dominio puede validar más)
        if (usuario.getNombre() == null || usuario.getNombre().isBlank())
            return Mono.error(new IllegalArgumentException("nombres requerido"));
        if (usuario.getApellido() == null || usuario.getApellido().isBlank())
            return Mono.error(new IllegalArgumentException("apellidos requerido"));
        if (usuario.getEmail() == null || usuario.getEmail().isBlank())
            return Mono.error(new IllegalArgumentException("correo_electronico requerido"));

        return usuarioRepository.existsByEmail(usuario.getEmail())
                .flatMap(exists -> {
                    if (exists) return Mono.error(new IllegalStateException("El correo ya existe"));
                    return usuarioRepository.save(usuario);
                });
    }

    public Mono<Usuario> updateUsuario(Usuario task) {
        return usuarioRepository.save(task);
    }

    public Flux<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Mono<Usuario> getUsuarioById(Integer  id) {
        return usuarioRepository.findById(id);
    }

    public Mono<Void> deleteUsuario(Integer  id) {
        return usuarioRepository.deleteById(id);
    }
}
