package co.com.pragma.autenticacion.api;

import java.time.LocalDate;

public record UsuarioResponseDTO(
        String idUsuario,
        String nombre,
        String apellido,
        String email,
        String documentoIdentidad,
        String direccion,
        LocalDate fechaNacimiento,
        String telefono,
        Long salarioBase,
        String rolNombre
) {}
