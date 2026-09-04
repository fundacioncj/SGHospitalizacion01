package com.ug.ec.SGHospitalizacion.interfaces;

import com.ug.ec.SGHospitalizacion.dto.request.ExamenMedicoRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.ExamenMedicoUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.ExamenMedicoResponseDTO;

import java.util.List;

public interface IExamenMedicoService {

    ExamenMedicoResponseDTO registrar(ExamenMedicoRequestDTO requestDTO);
    ExamenMedicoResponseDTO obtenerPorId(String id);
    ExamenMedicoResponseDTO actualizar(String id, ExamenMedicoUpdateRequestDTO requestDTO);
    void eliminar(String id);


    // * CONSULTAS ------------------------------------------------------------------

    List<ExamenMedicoResponseDTO> listarTodos();
    List<ExamenMedicoResponseDTO> listarPorHistoriaClinicaId(String historiaClinicaId);
    List<ExamenMedicoResponseDTO> listarPorNumeroHistoriaClinica(String numeroHistoriaClinica);
    List<ExamenMedicoResponseDTO> listarPorPacienteId(String pacienteId);
    List<ExamenMedicoResponseDTO> listarPorPacienteCedula(String cedula);
    List<ExamenMedicoResponseDTO> listarPorPacienteCedulaYNumeroHistoria(String cedula, String numeroHistoriaClinica);
    List<ExamenMedicoResponseDTO> listarPorMedicoId(String medicoId);
    List<ExamenMedicoResponseDTO> listarPorMedicoCedula(String cedula);

    byte[] generarPdf(String id);
}
