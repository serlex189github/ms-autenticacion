package co.com.pragma.autenticacion.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistrarUsuarioRequest(
        String nombre,
        String apellido,
        String documentoIdentidad,
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        String email,
        BigDecimal salarioBase,
        Integer idRol
) {}