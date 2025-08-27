package co.com.pragma.autenticacion.usecase.usuario;

import co.com.pragma.autenticacion.model.usuario.Usuario;
import co.com.pragma.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private  final UsuarioRepository usuarioRepository;

    public Mono<Usuario> saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
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
