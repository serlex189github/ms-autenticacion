package co.com.pragma.autenticacion.error;


import co.com.pragma.autenticacion.model.usuario.reglas.DomainValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static co.com.pragma.autenticacion.error.ErrorCodes.*;


@Configuration
public class GlobalExceptionHandlerConfig {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerConfig.class);

    @Bean
    @Order(-2) // se ejecuta antes del default handler de Spring
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectMapper mapper) {
        return (exchange, ex) -> {
            var req = exchange.getRequest();
            var res = exchange.getResponse();

            var m = mapException(ex);

            res.setStatusCode(m.status());
            res.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            var cid = res.getHeaders().getFirst("X-Correlation-Id");
            var body = new ApiError(
                    m.code(), m.message(), m.details(),
                    cid == null ? "" : cid,
                    OffsetDateTime.now(),
                    req.getPath().value()
            );

            byte[] json;
            try {
                json = mapper.writeValueAsBytes(body);
            } catch (Exception ser) {
                json = ("{\"codigo\":\"" + UNEXPECTED_ERROR + "\",\"mensaje\":\"Error serializando respuesta\"}")
                        .getBytes(StandardCharsets.UTF_8);
            }

            switch (m.level()) {
                case INFO -> log.info("[{}] {} {} -> {} {}", m.code(), req.getMethod(), req.getPath(), m.status().value(), m.message());
                case WARN -> log.warn("[{}] {} {} -> {} {}", m.code(), req.getMethod(), req.getPath(), m.status().value(), m.message());
                case ERROR -> log.error("[{}] {} {} -> {} {}", m.code(), req.getMethod(), req.getPath(), m.status().value(), m.message(), ex);
            }

            var buffer = res.bufferFactory().wrap(json);
            return res.writeWith(Mono.just(buffer));
        };
    }

    // ----------------- mapeo -----------------

    private record Mapped(HttpStatus status, String code, String message, List<String> details, Level level) {}
    private enum Level { INFO, WARN, ERROR }

    private Mapped mapException(Throwable ex) {
        // 400: validación de dominio (reglas de negocio)
        if (ex instanceof DomainValidationException dve) {
            var d = dve.getViolations().stream().map(v -> v.field() + ": " + v.message()).toList();
            return new Mapped(HttpStatus.BAD_REQUEST, VALIDATION_ERROR, "La solicitud es inválida", d, Level.WARN);
        }
        // 400: validaciones técnicas/entrada
        if (ex instanceof IllegalArgumentException || ex instanceof ServerWebInputException) {
            return new Mapped(HttpStatus.BAD_REQUEST, VALIDATION_ERROR, "La solicitud es inválida", details(ex), Level.WARN);
        }
        if (ex instanceof WebExchangeBindException bind) {
            var d = bind.getFieldErrors().stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
            return new Mapped(HttpStatus.BAD_REQUEST, VALIDATION_ERROR, "La solicitud es inválida", d, Level.WARN);
        }
        // 409: conflicto de negocio (email duplicado, etc.)
        if (ex instanceof DuplicateKeyException || ex instanceof IllegalStateException) {
            return new Mapped(HttpStatus.CONFLICT, BUSINESS_CONFLICT, "La operación entra en conflicto con el estado actual", details(ex), Level.WARN);
        }
        // 405/415
        if (ex instanceof MethodNotAllowedException) {
            return new Mapped(HttpStatus.METHOD_NOT_ALLOWED, METHOD_NOT_ALLOWED, "Método no permitido", details(ex), Level.INFO);
        }
        if (ex instanceof UnsupportedMediaTypeStatusException) {
            return new Mapped(HttpStatus.UNSUPPORTED_MEDIA_TYPE, UNSUPPORTED_MEDIA, "Tipo de contenido no soportado", details(ex), Level.WARN);
        }
        // 4xx/5xx con ResponseStatusException
        if (ex instanceof ResponseStatusException rse) {
            var st = HttpStatus.valueOf(rse.getStatusCode().value());
            var code = (st == HttpStatus.NOT_FOUND) ? NOT_FOUND : UNEXPECTED_ERROR;
            var msg = rse.getReason() != null ? rse.getReason() : (st == HttpStatus.NOT_FOUND ? "Recurso no encontrado" : "Error inesperado");
            return new Mapped(st, code, msg, details(ex), st.is4xxClientError() ? Level.WARN : Level.ERROR);
        }
        // 500: por defecto
        return new Mapped(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR, "Ocurrió un error inesperado", List.of(), Level.ERROR);
    }

    private List<String> details(Throwable ex) {
        var list = new ArrayList<String>();
        if (ex != null && ex.getMessage() != null && !ex.getMessage().isBlank()) list.add(ex.getMessage());
        return list;
    }
}
