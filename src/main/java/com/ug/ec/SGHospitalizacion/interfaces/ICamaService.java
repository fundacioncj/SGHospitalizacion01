package com.ug.ec.SGHospitalizacion.interfaces;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoCama;
import com.ug.ec.SGHospitalizacion.dto.request.CamaEstadoUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.CamaRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.CamaResponseDTO;

import java.util.List;

public interface ICamaService {
    CamaResponseDTO registrar(CamaRequestDTO requestDTO, String adminId);

    CamaResponseDTO obtenerPorId(String id);

    CamaResponseDTO obtenerPorNumeroCama(String numeroCama);

    List<CamaResponseDTO> listarTodas();

    List<CamaResponseDTO> listarPorEstado(EstadoCama estado);

    List<CamaResponseDTO> listarPorSala(String sala);

    // Caso de uso central del ticket: admin ve qué camas hay disponibles en una sala
    List<CamaResponseDTO> listarDisponiblesPorSala(String sala);

    // Solo permite DISPONIBLE o MANTENIMIENTO; OCUPADA la gestiona hospitalizacion
    CamaResponseDTO actualizarEstado(String id, CamaEstadoUpdateRequestDTO requestDTO, String adminId);

}
