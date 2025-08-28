package co.com.pragma.autenticacion.api;

import co.com.pragma.autenticacion.api.config.UsuarioPath;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UsuarioPath usuarioPath;
    private final UsuarioHandler usuarioHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    beanClass = UsuarioHandler.class,
                    beanMethod = "listenSaveUsuario",
                    operation = @Operation(
                            operationId = "saveUsuario",
                            summary = "Registrar nuevo usuario/solicitante",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = UsuarioRequestDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado",
                                            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                                    @ApiResponse(responseCode = "409", description = "Email ya registrado"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler usuarioHandler) {
        return route(POST(usuarioPath.getUsuarios()), this.usuarioHandler::listenSaveUsuario);
    }
}
