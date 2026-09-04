package com.ug.ec.SGHospitalizacion.mapper;

import com.ug.ec.SGHospitalizacion.domain.Hospitalizacion;
import com.ug.ec.SGHospitalizacion.dto.request.HospitalizacionRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.HospitalizacionUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.HospitalizacionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        // Los campos null del UpdateDTO no sobreescriben valores existentes en la entidad
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)

public interface HospitalizacionMapper {
    // DTO → ENTITY  (campos generados por el sistema se ignoran aquí)
    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "numeroIngreso", ignore = true)
    @Mapping(target = "estado",        ignore = true)
    @Mapping(target = "fechaEgreso",   ignore = true)
    // pacienteId/medicoId ya no vienen en el request (ahora es cedulaPaciente/cedulaMedico);
    // el service los resuelve consultando SGBITAMEDICA03 y los setea manualmente en la entidad.
    @Mapping(target = "pacienteId",    ignore = true)
    @Mapping(target = "medicoId",      ignore = true)
    Hospitalizacion toEntity(HospitalizacionRequestDTO dto);

    // ENTITY → RESPONSE DTO
    // nombrePaciente, cedulaPaciente y nombreMedico son enriquecidos
    // manualmente en el servicio (no existen en la entidad)
    @Mapping(target = "nombrePaciente", ignore = true)
    @Mapping(target = "cedulaPaciente", ignore = true)
    @Mapping(target = "nombreMedico",   ignore = true)
    HospitalizacionResponseDTO toDTO(Hospitalizacion hospitalizacion);

    // UPDATE  (solo modifica los campos permitidos; ignora los inmutables)
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "numeroIngreso",  ignore = true)
    @Mapping(target = "pacienteId",     ignore = true)
    @Mapping(target = "medicoId",       ignore = true)
    @Mapping(target = "fechaIngreso",   ignore = true)
    @Mapping(target = "motivoIngreso",  ignore = true)
    void updateEntity(HospitalizacionUpdateRequestDTO dto,
                      @MappingTarget Hospitalizacion hospitalizacion);
}
