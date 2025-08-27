package co.com.pragma.autenticacion.api;

import co.com.pragma.autenticacion.model.usuario.Usuario;
import co.com.pragma.autenticacion.model.usuario.UsuarioFactory;
import co.com.pragma.autenticacion.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.transaction.reactive.TransactionalOperator;

@Component
@RequiredArgsConstructor
public class UsuarioHandler {

    private static final Logger log = LoggerFactory.getLogger(UsuarioHandler.class);
    private final TransactionalOperator transactionalOperator;
    private final UsuarioUseCase usuarioUseCase; // o tu fachada/repos

//    public Mono<ServerResponse> listenSaveUsuario(ServerRequest req) {
//
//        log.debug("registrar-usuario:request:start path={} method={}",
//                req.path(), req.methodName());
//
//        return req.bodyToMono(Usuario.class)
//                .doOnNext(u -> log.debug("registrar-usuario:payload email={}", u.getEmail()))
//                .map(r -> UsuarioFactory.create(
//                        r.getNombre(), r.getApellido(), r.getFechaNacimiento(),
//                        r.getDireccion(), r.getTelefono(), r.getEmail(),
//                        r.getSalarioBase(), r.getIdRol()))
//                .flatMap(usuarioUseCase::saveUsuario)
//                .as(transactionalOperator::transactional)
//                .flatMap(saved -> ServerResponse
//                        .created(req.uriBuilder().path("/{id}").build(saved.getIdUsuario()))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(saved));
//    }

    public Mono<ServerResponse> listenSaveUsuario(ServerRequest req) {
        log.debug("registrar-usuario:request:start path={} method={}", req.path(), req.method().name());

        return req.bodyToMono(RegistrarUsuarioRequest.class)
                .doOnNext(r -> log.debug("registrar-usuario:payload email={}", r.email()))
                .map(r -> UsuarioFactory.create(
                        r.nombre(),
                        r.apellido(),
                        r.fechaNacimiento(),
                        r.direccion(),
                        r.telefono(),
                        r.email(),
                        r.documentoIdentidad(),
                        r.salarioBase(),
                        r.idRol()
                ))
                .flatMap(usuarioUseCase::saveUsuario)
                .as(transactionalOperator::transactional)
                .flatMap(saved -> ServerResponse
                        .created(req.uriBuilder().path("/{id}").build(saved.getIdUsuario()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(saved));
    }

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
