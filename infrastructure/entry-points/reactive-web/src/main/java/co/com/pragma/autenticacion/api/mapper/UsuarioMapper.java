package co.com.pragma.autenticacion.api.mapper;


import co.com.pragma.autenticacion.api.UsuarioRequestDTO;
import co.com.pragma.autenticacion.model.usuario.Usuario;
import co.com.pragma.autenticacion.model.usuario.UsuarioFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

// Si quieres que MapStruct registre el mapper como bean de Spring:
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    /**
     * Mapea el DTO de entrada al agregado/entidad de dominio.
     * No necesitas listar campo por campo aquí; delegamos la construcción a la factory.
     * Si luego agregas más campos, la factory es la única a tocar.
     */
    @Mapping(target = "idUsuario", ignore = true) // si el ID lo genera la capa de persistencia
    Usuario toDomain(UsuarioRequestDTO dto);

    /**
     * @ObjectFactory le dice a MapStruct cómo instanciar el objeto de destino (Usuario).
     * Aquí centralizas la llamada a la factory de dominio para respetar invariantes.
     */
    @ObjectFactory
    default Usuario newUsuario(UsuarioRequestDTO dto) {
        // Si fechaNacimiento en el DTO ya es LocalDate, esto funciona tal cual.
        // Si fuese String, ver "Caso B" más abajo.
        return UsuarioFactory.create(
                dto.nombre(),
                dto.apellido(),
                dto.fechaNacimiento(),
                dto.direccion(),
                dto.telefono(),
                dto.email(),
                dto.documentoIdentidad(),
                dto.salarioBase(),
                dto.idRol()
        );
    }
}