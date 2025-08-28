package co.com.pragma.autenticacion.model.usuario;


import co.com.pragma.autenticacion.model.usuario.reglas.DomainValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioFactoryTest {

    // ===== Helpers con datos válidos por defecto =====
    private String nombres() { return "  Juan  "; } // con espacios para verificar trim
    private String apellidos() { return "  Pérez  "; }
    private LocalDate fechaNacimiento() { return LocalDate.of(1990, 1, 15); }
    private String direccion() { return " Calle 123 "; }
    private String telefono() { return " 3001112233 "; }
    private String email() { return "  juan.perez@mail.com  "; } // para verificar trim
    private String documentoIdentidad() { return "10101010"; }
    private BigDecimal salarioBase() { return new BigDecimal("1500000"); }
    private Integer idRol() { return 2; }

    // ===== Caso feliz =====
    @Test
    @DisplayName("create(): con datos válidos retorna Usuario con trims aplicados y opcionales normalizados")
    void create_ok() {
        Usuario usuario = UsuarioFactory.create(
                nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                email(), documentoIdentidad(), salarioBase(), idRol()
        );

        // Campos obligatorios (trim aplicados)
        assertThat(usuario.getNombre()).isEqualTo("Juan");
        assertThat(usuario.getApellido()).isEqualTo("Pérez");
        assertThat(usuario.getEmail()).isEqualTo("juan.perez@mail.com");

        // Campos opcionales: no deben convertirse a vacío
        assertThat(usuario.getDireccion()).isEqualTo(" Calle 123 "); // la factory no trimmea dir/tel
        assertThat(usuario.getTelefono()).isEqualTo(" 3001112233 ");

        // Resto de campos
        assertThat(usuario.getFechaNacimiento()).isEqualTo(fechaNacimiento());
        assertThat(usuario.getDocumentoIdentidad()).isEqualTo(documentoIdentidad());
        assertThat(usuario.getSalarioBase()).isEqualTo(salarioBase());
        assertThat(usuario.getIdRol()).isEqualTo(idRol());

        // ID lo asignará la persistencia; aquí debe venir null
        assertThat(usuario.getIdUsuario()).isNull();
    }

    // ===== Normalización de opcionales =====
    @Test
    @DisplayName("create(): direccion/telefono en blanco -> se asigna null")
    void create_opcionalesBlanco_aNull() {
        Usuario usuario = UsuarioFactory.create(
                nombres(), apellidos(), fechaNacimiento(), "   ", " ",
                email(), documentoIdentidad(), salarioBase(), idRol()
        );

        assertThat(usuario.getDireccion()).isNull();
        assertThat(usuario.getTelefono()).isNull();
    }

    // ===== Límites de salario =====
    @Test
    @DisplayName("create(): salarioBase = 0 permitido")
    void create_salarioMinimoPermitido() {
        Usuario usuario = UsuarioFactory.create(
                nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                email(), documentoIdentidad(), new BigDecimal("0"), idRol()
        );
        assertThat(usuario.getSalarioBase()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("create(): salarioBase = 15000000 permitido")
    void create_salarioMaximoPermitido() {
        Usuario usuario = UsuarioFactory.create(
                nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                email(), documentoIdentidad(), new BigDecimal("15000000"), idRol()
        );
        assertThat(usuario.getSalarioBase()).isEqualByComparingTo("15000000");
    }

    // ===== Validaciones obligatorios =====
    @Test
    @DisplayName("create(): nombres null -> DomainValidationException")
    void create_nombresNull() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        null, apellidos(), fechaNacimiento(), direccion(), telefono(),
                        email(), documentoIdentidad(), salarioBase(), idRol()
                )
        );
    }

    @Test
    @DisplayName("create(): nombres en blanco -> DomainValidationException")
    void create_nombresBlanco() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        "   ", apellidos(), fechaNacimiento(), direccion(), telefono(),
                        email(), documentoIdentidad(), salarioBase(), idRol()
                )
        );
    }

    @Test
    @DisplayName("create(): apellidos null -> DomainValidationException")
    void create_apellidosNull() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), null, fechaNacimiento(), direccion(), telefono(),
                        email(), documentoIdentidad(), salarioBase(), idRol()
                )
        );
    }

    @Test
    @DisplayName("create(): apellidos en blanco -> DomainValidationException")
    void create_apellidosBlanco() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), "   ", fechaNacimiento(), direccion(), telefono(),
                        email(), documentoIdentidad(), salarioBase(), idRol()
                )
        );
    }

    @Test
    @DisplayName("create(): documentoIdentidad null o blank -> DomainValidationException")
    void create_documentoIdentidadInvalido() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                        email(), null, salarioBase(), idRol()
                )
        );
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                        email(), "   ", salarioBase(), idRol()
                )
        );
    }

    // ===== Email =====
    @Test
    @DisplayName("create(): email null -> DomainValidationException")
    void create_emailNull() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                        null, documentoIdentidad(), salarioBase(), idRol()
                )
        );
    }

    @Test
    @DisplayName("create(): email blank -> DomainValidationException")
    void create_emailBlank() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                        "   ", documentoIdentidad(), salarioBase(), idRol()
                )
        );
    }

    @Test
    @DisplayName("create(): email inválido -> DomainValidationException")
    void create_emailInvalido() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                        "correo@invalido", documentoIdentidad(), salarioBase(), idRol()
                )
        );
    }

    // ===== Salario =====
    @Test
    @DisplayName("create(): salarioBase null -> DomainValidationException")
    void create_salarioNull() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                        email(), documentoIdentidad(), null, idRol()
                )
        );
    }

    @Test
    @DisplayName("create(): salarioBase < 0 -> DomainValidationException")
    void create_salarioMenorQueCero() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                        email(), documentoIdentidad(), new BigDecimal("-0.01"), idRol()
                )
        );
    }

    @Test
    @DisplayName("create(): salarioBase > 15000000 -> DomainValidationException")
    void create_salarioMayorMaximo() {
        assertThrows(DomainValidationException.class, () ->
                UsuarioFactory.create(
                        nombres(), apellidos(), fechaNacimiento(), direccion(), telefono(),
                        email(), documentoIdentidad(), new BigDecimal("15000000.01"), idRol()
                )
        );
    }

}
