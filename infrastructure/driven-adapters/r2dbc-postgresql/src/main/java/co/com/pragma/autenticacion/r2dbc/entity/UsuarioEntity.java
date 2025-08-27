package co.com.pragma.autenticacion.r2dbc.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Table("usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsuarioEntity {

    @Id
    @Column("id_usuario")
    private Integer idUsuario;

    @Column("nombre")
    private String nombre;

    @Column("apellido")
    private String apellido;

    @Column("email")
    private String email;

    @Column("documento_identidad")
    private String documentoIdentidad;

    @Column("telefono")
    private String telefono;

    @Column("direccion")
    private String direccion;

    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column("salario_base")
    private BigDecimal salarioBase;

    @Column("id_rol")
    private Integer idRol;
}