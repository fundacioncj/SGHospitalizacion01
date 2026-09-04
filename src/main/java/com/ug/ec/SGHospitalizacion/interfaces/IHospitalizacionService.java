package com.ug.ec.SGHospitalizacion.interfaces;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoHospitalizacion;
import com.ug.ec.SGHospitalizacion.dto.external.MedicoExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.request.HospitalizacionRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.HospitalizacionUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.HospitalizacionResponseDTO;

import java.util.List;

public interface IHospitalizacionService {
    HospitalizacionResponseDTO registrar(HospitalizacionRequestDTO requestDTO);

    HospitalizacionResponseDTO obtenerPorId(String id);

    HospitalizacionResponseDTO obtenerPorNumeroIngreso(String numeroIngreso);

    List<HospitalizacionResponseDTO> listarTodas();

    List<HospitalizacionResponseDTO> listarPorEstado(EstadoHospitalizacion estado);

    List<HospitalizacionResponseDTO> listarPorPaciente(String pacienteId);

    List<HospitalizacionResponseDTO> listarPorMedico(String medicoId);

    List<HospitalizacionResponseDTO> listarPorPacienteCedula(String cedula);

    List<HospitalizacionResponseDTO> listarPorMedicoCedula(String cedula);

    List<MedicoExternoDTO> listarMedicos();

    HospitalizacionResponseDTO actualizar(String id, HospitalizacionUpdateRequestDTO requestDTO);
}
