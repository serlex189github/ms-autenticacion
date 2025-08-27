package co.com.pragma.autenticacion.model.usuario;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {

    private Integer idUsuario;

    private String nombre;

    private String apellido;

    private String email;

    private String documentoIdentidad;

    private String telefono;

    private String direccion;

    private LocalDate fechaNacimiento;

    private BigDecimal salarioBase;

    private Integer idRol;
}