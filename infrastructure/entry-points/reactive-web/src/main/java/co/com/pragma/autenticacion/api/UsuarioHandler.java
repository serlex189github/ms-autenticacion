package co.com.pragma.autenticacion.api;

import co.com.pragma.autenticacion.api.mapper.UsuarioMapper;
import co.com.pragma.autenticacion.model.usuario.UsuarioFactory;
import co.com.pragma.autenticacion.usecase.usuario.UsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;

    @Operation(
            summary = "Registrar usuario",
            description = "Recibe un objeto UsuarioRequestDTO y guarda un usuario en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario guardado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Mono<ServerResponse> listenSaveUsuario(ServerRequest req) {
        log.debug("registrar-usuario:request:start path={} method={}", req.path(), req.method().name());

        return req.bodyToMono(UsuarioRequestDTO.class)
                .doOnNext(r -> log.debug("registrar-usuario:payload email={}", r.email()))
                .map(usuarioMapper::toDomain)
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
