package co.com.pragma.autenticacion.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UsuarioRequestDTO(
        @Schema(
                description = "Nombre del usuario",
                example = "Carlos",
                minLength = 2,
                maxLength = 50
        )
        String nombre,

        @Schema(
                description = "Apellido del usuario",
                example = "Pérez",
                minLength = 2,
                maxLength = 50
        )
        String apellido,

        @Schema(
                description = "Número de documento de identidad",
                example = "1234567890",
                minLength = 6,
                maxLength = 20
        )
        String documentoIdentidad,

        @Schema(
                description = "Fecha de nacimiento en formato ISO 8601 (yyyy-MM-dd)",
                example = "1995-10-01",
                type = "string",
                format = "date"
        )
        LocalDate fechaNacimiento,

        @Schema(
                description = "Dirección de residencia del usuario",
                example = "Av. Central 123",
                minLength = 5,
                maxLength = 100
        )
        String direccion,

        @Schema(
                description = "Teléfono de contacto del usuario",
                example = "+57 3001234567",
                pattern = "^\\+?[0-9 ]{7,15}$"
        )
        String telefono,

        @Schema(
                description = "Email",
                example = "carlos.perez@example.com",
                format = "email"
        )
        String email,

        @Schema(
                description = "Salario base del usuario",
                example = "2500000",
                minimum = "0"
        )
        BigDecimal salarioBase,

        @Schema(
                description = "Identificador del rol asociado al usuario",
                example = "2",
                type = "integer",
                format = "int32",
                minimum = "1"
        )
        Integer idRol
) {}