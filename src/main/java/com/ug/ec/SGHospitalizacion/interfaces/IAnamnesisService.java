package com.ug.ec.SGHospitalizacion.interfaces;

import com.ug.ec.SGHospitalizacion.dto.request.AnamnesisRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.AnamnesisUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.AnamnesisResponseDTO;

import java.util.List;

public interface IAnamnesisService {

    AnamnesisResponseDTO registrar(AnamnesisRequestDTO requestDTO);
    AnamnesisResponseDTO obtenerPorId(String id);
    AnamnesisResponseDTO actualizar(String id, AnamnesisUpdateRequestDTO requestDTO);
    void eliminar(String id);

    // * CONSULTAS ------------------------------------------------------------------

    List<AnamnesisResponseDTO> listarTodas();
    List<AnamnesisResponseDTO> obtenerPorHistoriaClinica(String historiaClinicaId); // ACTUALIZANDOO
    List<AnamnesisResponseDTO> listarPorPaciente(String pacienteId);
    List<AnamnesisResponseDTO> listarPorMedico(String medicoId);
    List<AnamnesisResponseDTO> listarPorPacienteCedula(String cedula);
    List<AnamnesisResponseDTO> listarPorMedicoCedula(String cedula);
}
