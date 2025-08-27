package co.com.pragma.autenticacion.model.usuario.reglas;

import java.util.List;

public class DomainValidationException extends RuntimeException {

    public record Validacion(String field, String message) {}
    private final List<Validacion> validaciones;

    public DomainValidationException(List<Validacion> validaciones) {
        super("Validación de dominio fallida");
        this.validaciones = List.copyOf(validaciones);
    }
    public List<Validacion> getViolations() { return validaciones; }
}
