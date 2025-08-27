package co.com.pragma.autenticacion.r2dbc;

import co.com.pragma.autenticacion.model.usuario.Usuario;
import co.com.pragma.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.autenticacion.r2dbc.entity.UsuarioEntity;
import co.com.pragma.autenticacion.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        Integer ,
        UsuarioReactiveRepository
        > implements UsuarioRepository {
    public UsuarioRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Usuario.class));
    }

    @Override
    public Mono<Boolean> existsByCorreo(String correo) {
        return null;
    }

    public Mono<Usuario> save(Usuario usuario) {
        return super.save(usuario);
    }

    @Override
    public Mono<Usuario> update(Usuario usuario) {
        return null;
    }

    @Override
    public Flux<Usuario> findAll() {
        return super.findAll();
    }

    @Override
    public Mono<Usuario> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public Mono<Usuario> findByCorreo(String correo) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return repository.deleteById(id);
    }

}