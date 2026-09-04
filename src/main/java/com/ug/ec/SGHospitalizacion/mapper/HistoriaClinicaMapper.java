package com.ug.ec.SGHospitalizacion.mapper;

import com.ug.ec.SGHospitalizacion.domain.HistoriaClinica;
import com.ug.ec.SGHospitalizacion.dto.request.HistoriaClinicaUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.HistoriaClinicaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HistoriaClinicaMapper {

    HistoriaClinicaResponseDTO toDTO(HistoriaClinica historiaClinica);

    @Mapping(target = "activo", source = "activo")
    void updateEntity(HistoriaClinicaUpdateRequestDTO dto,
                      @MappingTarget HistoriaClinica historiaClinica);
}
