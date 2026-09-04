package com.ug.ec.SGHospitalizacion.interfaces;

import com.ug.ec.SGHospitalizacion.dto.request.HistoriaClinicaUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.HistoriaClinicaResponseDTO;

import java.util.List;

public interface IHistoriaClinicaService {

    /** Abre (o devuelve existente) una HC para el paciente con ID de SGBITAMEDICA03 */
    HistoriaClinicaResponseDTO abrirParaPaciente(String pacienteId);

    /** Abre (o devuelve existente) una HC para el paciente identificado por su cédula */
    HistoriaClinicaResponseDTO abrirParaPacienteCedula(String cedula);

    List<HistoriaClinicaResponseDTO> listarTodas();

    HistoriaClinicaResponseDTO obtenerPorId(String id);

    HistoriaClinicaResponseDTO obtenerPorPacienteId(String pacienteId);

    HistoriaClinicaResponseDTO obtenerPorCedulaPaciente(String cedula);

    HistoriaClinicaResponseDTO actualizar(String id, HistoriaClinicaUpdateRequestDTO requestDTO);


}
