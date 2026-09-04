package com.ug.ec.SGHospitalizacion.mapper;

import com.ug.ec.SGHospitalizacion.domain.Anamnesis;
import com.ug.ec.SGHospitalizacion.domain.GinecoObstetricosAndrologicos;
import com.ug.ec.SGHospitalizacion.dto.request.AnamnesisRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.AnamnesisUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.GinecoObstetricosAndrologicosRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.AnamnesisResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AnamnesisMapper {

    // * REQUEST DTO → ENTITY -----------------------------------------------------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "historiaClinicaId", ignore = true)
    // pacienteId/medicoId ya no vienen en el request (ahora es cedulaPaciente/cedulaMedico);
    // el service los resuelve consultando SGBITAMEDICA03 y los setea manualmente en la entidad.
    @Mapping(target = "pacienteId", ignore = true)
    @Mapping(target = "medicoId", ignore = true)
    Anamnesis toEntity(AnamnesisRequestDTO dto);

    // * ENTITY → RESPONSE DTO ----------------------------------------------------------
    @Mapping(target = "nombrePaciente", ignore = true)
    @Mapping(target = "cedulaPaciente", ignore = true)
    @Mapping(target = "nombreMedico", ignore = true)
    AnamnesisResponseDTO toDTO(Anamnesis anamnesis);

    // * UPDATE DTO → ENTITY (solo modifica campos permitidos) --------------------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pacienteId", ignore = true)
    @Mapping(target = "medicoId", ignore = true)
    @Mapping(target = "historiaClinicaId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    void updateEntity(AnamnesisUpdateRequestDTO dto, @MappingTarget Anamnesis anamnesis);

    // * SUBDOCUMENTO -------------------------------------------------------------------
    GinecoObstetricosAndrologicos toSubdocumento(GinecoObstetricosAndrologicosRequestDTO dto);

    GinecoObstetricosAndrologicosRequestDTO toSubdocumentoDTO(GinecoObstetricosAndrologicos subdoc);
}
