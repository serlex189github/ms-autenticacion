package co.com.pragma.autenticacion.error;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
        String codigo,
        String mensaje,
        List<String> detalles,
        String correlationId,
        OffsetDateTime timestamp,
        String path
) {}

