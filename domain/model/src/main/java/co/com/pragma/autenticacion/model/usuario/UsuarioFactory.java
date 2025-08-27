package co.com.pragma.autenticacion.model.usuario;

import co.com.pragma.autenticacion.model.usuario.reglas.DomainValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class UsuarioFactory {

    private static final BigDecimal MIN = new BigDecimal("0");
    private static final BigDecimal MAX = new BigDecimal("15000000");
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private UsuarioFactory() {}

    public static Usuario create(String nombres,
                                 String apellidos,
                                 LocalDate fechaNacimiento,
                                 String direccion,
                                 String telefono,
                                 String email,
                                 BigDecimal salarioBase,
                                 Integer idRol) {

        List<DomainValidationException.Validacion> errors = new ArrayList<>();

        if (nombres == null || nombres.isBlank())
            errors.add(new DomainValidationException.Validacion("nombres", "nombres es requerido"));
        if (apellidos == null || apellidos.isBlank())
            errors.add(new DomainValidationException.Validacion("apellidos", "apellidos es requerido"));

        if (email == null || email.isBlank()) {
            errors.add(new DomainValidationException.Validacion("correo_electronico", "correo_electronico es requerido"));
        } else if (!EMAIL.matcher(email.trim()).matches()) {
            errors.add(new DomainValidationException.Validacion("correo_electronico", "correo_electronico inválido"));
        }

        if (salarioBase == null) {
            errors.add(new DomainValidationException.Validacion("salario_base", "salario_base es requerido"));
        } else if (salarioBase.compareTo(MIN) < 0 || salarioBase.compareTo(MAX) > 0) {
            errors.add(new DomainValidationException.Validacion("salario_base", "salario_base debe estar entre 0 y 15000000"));
        }

        if (!errors.isEmpty()) throw new DomainValidationException(errors);

        String dir = (direccion == null || direccion.isBlank()) ? null : direccion;
        String tel = (telefono  == null || telefono.isBlank())  ? null : telefono;

        return Usuario.builder()
                .nombre(nombres.trim())
                .apellido(apellidos.trim())
                .fechaNacimiento(fechaNacimiento)
                .direccion(dir)
                .telefono(tel)
                .email(email.trim())
                .salarioBase(salarioBase)
                .idRol(idRol)
                .build();
    }
}
