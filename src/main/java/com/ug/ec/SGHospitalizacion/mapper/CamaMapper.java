package com.ug.ec.SGHospitalizacion.mapper;

import com.ug.ec.SGHospitalizacion.domain.Cama;
import com.ug.ec.SGHospitalizacion.dto.request.CamaEstadoUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.CamaRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.CamaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CamaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "hospitalizacionId", ignore = true)
    Cama toEntity(CamaRequestDTO dto);
    CamaResponseDTO toDTO (Cama cama);

    //solo se permite accedeer al estado y descripcion, el resto de campos no pueden ser modificados
    //por el aministrador

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroCama", ignore = true)
    @Mapping(target = "sala", ignore = true)
    @Mapping(target = "piso", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "hospitalizacionId", ignore = true)
    void actualizarEstado (CamaEstadoUpdateRequestDTO dto, @MappingTarget Cama cama);
}
