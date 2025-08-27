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
    private static final BigDecimal SALARIO_MIN = new BigDecimal("0");
    private static final BigDecimal SALARIO_MAX = new BigDecimal("15000000");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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