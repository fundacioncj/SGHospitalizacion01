package com.ug.ec.SGHospitalizacion.mapper;

import com.ug.ec.SGHospitalizacion.domain.DiagnosticoMedico;
import com.ug.ec.SGHospitalizacion.domain.ExamenMedico;
import com.ug.ec.SGHospitalizacion.dto.request.DiagnosticoMedicoRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.ExamenMedicoRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.ExamenMedicoUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.ExamenMedicoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamenMedicoMapper {

    // * REQUEST DTO → ENTITY -----------------------------------------------------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pacienteId", ignore = true)
    @Mapping(target = "medicoId", ignore = true)
    @Mapping(target = "historiaClinicaId", ignore = true)
    ExamenMedico toEntity(ExamenMedicoRequestDTO dto);


    // * ENTITY → RESPONSE DTO ----------------------------------------------------------
    @Mapping(target = "nombrePaciente", ignore = true)
    @Mapping(target = "cedulaPaciente", ignore = true)
    @Mapping(target = "nombreMedico", ignore = true)
    ExamenMedicoResponseDTO toDTO(ExamenMedico examenMedico);


    // * UPDATE DTO → ENTITY ------------------------------------------------------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pacienteId", ignore = true)
    @Mapping(target = "medicoId", ignore = true)
    @Mapping(target = "historiaClinicaId", ignore = true)
    @Mapping(target = "numeroHistoriaClinica", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    void updateEntity(ExamenMedicoUpdateRequestDTO dto, @MappingTarget ExamenMedico examenMedico);


    // * SUBDOCUMENTO: DIAGNÓSTICO -------------------------------------------------------
    DiagnosticoMedico toSubdocumento(DiagnosticoMedicoRequestDTO dto);

    DiagnosticoMedicoRequestDTO toSubdocumentoDTO(DiagnosticoMedico subdoc);
}