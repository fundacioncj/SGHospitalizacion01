package com.ug.ec.SGHospitalizacion.service;

import com.ug.ec.SGHospitalizacion.dao.HistoriaClinicaRepository;
import com.ug.ec.SGHospitalizacion.domain.HistoriaClinica;
import com.ug.ec.SGHospitalizacion.dto.external.PacienteExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.request.HistoriaClinicaUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.HistoriaClinicaResponseDTO;
import com.ug.ec.SGHospitalizacion.exception.RecursoNoEncontradoException;
import com.ug.ec.SGHospitalizacion.infrastructure.client.SgbCitaMedicaClient;
import com.ug.ec.SGHospitalizacion.interfaces.IHistoriaClinicaService;
import com.ug.ec.SGHospitalizacion.mapper.HistoriaClinicaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoriaClinicaServiceImpl implements IHistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final HistoriaClinicaMapper     historiaClinicaMapper;
    private final SgbCitaMedicaClient       sgbClient;            // ← integración REST

    // ── APERTURA MANUAL DE HC ─────────────────────────────────────────────────

    @Override
    public HistoriaClinicaResponseDTO abrirParaPaciente(String pacienteId) {
        // Validar que el paciente existe y está activo en el sistema base
        sgbClient.obtenerPacienteActivoPorId(pacienteId);

        // Devolver HC existente o crear una nueva
        return historiaClinicaRepository.findByPacienteId(pacienteId)
                .map(historia -> enriquecerHistoriaClinica(
                        historiaClinicaMapper.toDTO(historia)))
                .orElseGet(() -> {
                    HistoriaClinica nueva = HistoriaClinica.builder()
                            .pacienteId(pacienteId)
                            .numeroHistoriaClinica(generarNumeroHC())
                            .fechaApertura(LocalDate.now())
                            .build();
                    HistoriaClinica guardada = historiaClinicaRepository.save(nueva);
                    log.info("Historia Clínica {} creada para paciente ID: {}",
                            guardada.getNumeroHistoriaClinica(), pacienteId);
                    return enriquecerHistoriaClinica(historiaClinicaMapper.toDTO(guardada));
                });
    }

    public List<HistoriaClinicaResponseDTO> listarTodas() {
        return historiaClinicaRepository.findAll().stream()
                .map(historiaClinicaMapper::toDTO)
                .map(this::enriquecerHistoriaClinica)
                .toList();
    }

    /**
     * Abre (o devuelve existente) una Historia Clínica para un paciente identificado por su cédula.
     * Resuelve la cédula al pacienteId (ID SQL) consultando SGBITAMEDICA03 y delega en abrirParaPaciente.
     */
    @Override
    public HistoriaClinicaResponseDTO abrirParaPacienteCedula(String cedula) {
        PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(cedula);
        return abrirParaPaciente(String.valueOf(paciente.getId()));
    }

    // ── CONSULTAS ─────────────────────────────────────────────────────────────

    @Override
    public HistoriaClinicaResponseDTO obtenerPorId(String id) {
        HistoriaClinica hc = buscarHCPorId(id);
        HistoriaClinicaResponseDTO dto = historiaClinicaMapper.toDTO(hc);
        return enriquecerHistoriaClinica(dto);
    }

    @Override
    public HistoriaClinicaResponseDTO obtenerPorPacienteId(String pacienteId) {
        HistoriaClinica hc = historiaClinicaRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Historia Clínica no encontrada para paciente ID: " + pacienteId));
        HistoriaClinicaResponseDTO dto = historiaClinicaMapper.toDTO(hc);

        return enriquecerHistoriaClinica(dto);
    }

    /**
     * Busca la HC de un paciente por su cédula, consultando primero el ID en SGBITAMEDICA03.
     */
    @Override
    public HistoriaClinicaResponseDTO obtenerPorCedulaPaciente(String cedula) {
        PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(cedula);
        return obtenerPorPacienteId(String.valueOf(paciente.getId()));
    }

    @Override
    public HistoriaClinicaResponseDTO actualizar(String id, HistoriaClinicaUpdateRequestDTO requestDTO) {
        log.info("Actualizando Historia Clínica ID: {}", id);
        HistoriaClinica hc = buscarHCPorId(id);
        historiaClinicaMapper.updateEntity(requestDTO, hc);
        HistoriaClinica hcActualizada = historiaClinicaRepository.save(hc);
        HistoriaClinicaResponseDTO dto = historiaClinicaMapper.toDTO(hcActualizada);

        return enriquecerHistoriaClinica(dto);
    }

    // ── Métodos privados ──────────────────────────────────────────────────────

    /** Genera número HC correlativo con formato HC-000001 */
    private String generarNumeroHC() {
        long total = historiaClinicaRepository.count();
        return "HC-" + String.format("%06d", total + 1);
    }

    public HistoriaClinica obtenerOCrearHC(String pacienteId) {
        return historiaClinicaRepository.findByPacienteId(pacienteId)
                .orElseGet(() -> {
                    HistoriaClinica nueva = HistoriaClinica.builder()
                            .pacienteId(pacienteId)
                            .numeroHistoriaClinica(generarNumeroHC())
                            .fechaApertura(LocalDate.now())
                            .build();
                    log.info("HC creada automáticamente para paciente ID: {}", pacienteId);
                    return historiaClinicaRepository.save(nueva);
                });
    }

    private HistoriaClinicaResponseDTO enriquecerHistoriaClinica(HistoriaClinicaResponseDTO dto) {

        try {

            // Primero obtenemos el paciente por ID para conocer su cédula
            PacienteExternoDTO pacienteId = sgbClient.obtenerPacientePorId(dto.getPacienteId());


            // Luego consultamos por cédula, que es donde viene la persona completa
            PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(pacienteId.getCedula());


            dto.setNombrePaciente(paciente.getNombreCompleto());
            dto.setCedulaPaciente(paciente.getCedula());
            log.info("PACIENTE ENRIQUECIDO: {}", paciente.getNombreCompleto());


        } catch (Exception e) {
            log.error("No se pudo obtener información del paciente ID {}", dto.getPacienteId(), e);
        }

        return dto;
    }

    private HistoriaClinica buscarHCPorId(String id) {
        return historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Historia Clínica no encontrada con ID: " + id));
    }
}
