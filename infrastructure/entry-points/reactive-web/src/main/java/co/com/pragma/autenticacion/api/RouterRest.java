package co.com.pragma.autenticacion.api;

import co.com.pragma.autenticacion.api.config.UsuarioPath;
import lombok.RequiredArgsConstructor;
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
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler usuarioHandler) {
        return route(POST(usuarioPath.getUsuarios()), this.usuarioHandler::listenSaveUsuario);
    }
}
