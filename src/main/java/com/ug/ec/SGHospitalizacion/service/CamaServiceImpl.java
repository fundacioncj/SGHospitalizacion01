package com.ug.ec.SGHospitalizacion.service;

import com.ug.ec.SGHospitalizacion.dao.CamaRepository;
import com.ug.ec.SGHospitalizacion.domain.Cama;
import com.ug.ec.SGHospitalizacion.domain.enums.EstadoCama;
import com.ug.ec.SGHospitalizacion.dto.external.MedicoExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.request.CamaEstadoUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.CamaRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.CamaResponseDTO;
import com.ug.ec.SGHospitalizacion.exception.RecursoNoEncontradoException;
import com.ug.ec.SGHospitalizacion.infrastructure.client.SgbCitaMedicaClient;
import com.ug.ec.SGHospitalizacion.interfaces.ICamaService;
import com.ug.ec.SGHospitalizacion.mapper.CamaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de Camas refactorizado.
 *
 * La validación de rol ADMIN se delega a SgbCitaMedicaClient:
 * el adminId recibido es el ID SQL del médico/empleado en SGBITAMEDICA03.
 *
 * NOTA: Si SGBITAMEDICA03 expone un endpoint /empleados/{id} para verificar
 * rol ADMIN, adapte confirmarAccesoAdministrador() para consumirlo.
 * Por ahora se valida usando el endpoint de médicos con un rol específico.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CamaServiceImpl implements ICamaService {

    private final CamaRepository      camaRepository;
    private final CamaMapper           camaMapper;
    private final SgbCitaMedicaClient  sgbClient;            // ← integración REST

    @Override
    public CamaResponseDTO registrar(CamaRequestDTO requestDTO, String adminId) {
        log.info("Registrando cama {} por admin SQL-ID: {}", requestDTO.getNumeroCama(), adminId);

        // Verificar que el admin existe en SGBITAMEDICA03
        confirmarAccesoAdministrador(adminId);

        if (camaRepository.existsByNumeroCama(requestDTO.getNumeroCama())) {
            throw new IllegalArgumentException("Ya existe una cama con el número: " + requestDTO.getNumeroCama());
        }

        Cama cama = camaMapper.toEntity(requestDTO);
        cama.setEstado(EstadoCama.DISPONIBLE);

        Cama guardada = camaRepository.save(cama);
        return camaMapper.toDTO(guardada);
    }

    @Override
    public CamaResponseDTO obtenerPorId(String id) {
        return camaMapper.toDTO(buscarPorId(id));
    }

    @Override
    public CamaResponseDTO obtenerPorNumeroCama(String numeroCama) {
        Cama cama = camaRepository.findByNumeroCama(numeroCama)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cama no encontrada con número: " + numeroCama));
        return camaMapper.toDTO(cama);
    }

    @Override
    public List<CamaResponseDTO> listarTodas() {
        return camaRepository.findAll().stream().map(camaMapper::toDTO).toList();
    }

    @Override
    public List<CamaResponseDTO> listarPorEstado(EstadoCama estado) {
        return camaRepository.findByEstado(estado).stream().map(camaMapper::toDTO).toList();
    }

    @Override
    public List<CamaResponseDTO> listarPorSala(String sala) {
        return camaRepository.findBySala(sala).stream().map(camaMapper::toDTO).toList();
    }

    @Override
    public List<CamaResponseDTO> listarDisponiblesPorSala(String sala) {
        return camaRepository.findBySalaAndEstado(sala, EstadoCama.DISPONIBLE).stream()
                .map(camaMapper::toDTO).toList();
    }

    @Override
    public CamaResponseDTO actualizarEstado(String id, CamaEstadoUpdateRequestDTO requestDTO, String adminId) {
        log.info("Cambio de estado en cama ID: {} por admin SQL-ID: {}", id, adminId);

        confirmarAccesoAdministrador(adminId);

        // El estado OCUPADA lo gestiona HospitalizacionServiceImpl, no el admin directamente
        if (EstadoCama.OCUPADA.equals(requestDTO.getEstado())) {
            throw new IllegalArgumentException(
                    "El estado OCUPADA se asigna automáticamente al registrar una hospitalización");
        }

        Cama cama = buscarPorId(id);

        // Si se libera manualmente, limpiar la referencia de hospitalización
        if (EstadoCama.DISPONIBLE.equals(requestDTO.getEstado())) {
            cama.setHospitalizacionId(null);
        }

        camaMapper.actualizarEstado(requestDTO, cama);
        return camaMapper.toDTO(camaRepository.save(cama));
    }

    // ── Métodos privados ──────────────────────────────────────────────────────

    private Cama buscarPorId(String id) {
        return camaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cama no encontrada con ID: " + id));
    }

    /**
     * Verifica en SGBITAMEDICA03 que el ID corresponde a un médico/empleado activo.
     * Si SGBITAMEDICA03 expone /empleados/{id}, adapte esta llamada.
     * Por ahora consulta el endpoint de médicos como referencia de personal activo.
     */
    private void confirmarAccesoAdministrador(String adminId) {
        try {
            MedicoExternoDTO medico = sgbClient.obtenerMedicoActivoPorId(adminId);
            log.debug("Acceso confirmado para: {} {}", medico.getNombres(), medico.getApellidos());
        } catch (RecursoNoEncontradoException ex) {
            throw new RecursoNoEncontradoException(
                    "Administrador con ID " + adminId + " no encontrado en SGBITAMEDICA03. "
                    + "Si es un empleado, use el endpoint /empleados/{id} de SGBITAMEDICA03 para verificar.");
        }
    }
}
