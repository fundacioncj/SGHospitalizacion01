package com.ug.ec.SGHospitalizacion.service;

import com.ug.ec.SGHospitalizacion.dao.AnamnesisRepository;
import com.ug.ec.SGHospitalizacion.dao.HistoriaClinicaRepository;
import com.ug.ec.SGHospitalizacion.domain.Anamnesis;
import com.ug.ec.SGHospitalizacion.domain.HistoriaClinica;
import com.ug.ec.SGHospitalizacion.dto.external.MedicoExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.external.PacienteExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.request.AnamnesisRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.AnamnesisUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.AnamnesisResponseDTO;
import com.ug.ec.SGHospitalizacion.exception.RecursoNoEncontradoException;
import com.ug.ec.SGHospitalizacion.infrastructure.client.SgbCitaMedicaClient;
import com.ug.ec.SGHospitalizacion.interfaces.IAnamnesisService;
import com.ug.ec.SGHospitalizacion.mapper.AnamnesisMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de Anamnesis refactorizado para DDD con integración REST a SGBITAMEDICA03.
 *
 * - pacienteId y medicoId son IDs SQL del sistema base (SGBITAMEDICA03).
 * - La validación de existencia y estado activo se delega al SgbCitaMedicaClient.
 * - La Historia Clínica se busca por pacienteId en MongoDB.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnamnesisServiceImpl implements IAnamnesisService {

    private final AnamnesisRepository        anamnesisRepository;
    private final HistoriaClinicaRepository  historiaClinicaRepository;
    private final AnamnesisMapper            anamnesisMapper;
    private final SgbCitaMedicaClient        sgbClient;            // ← integración REST

    @Override
    public AnamnesisResponseDTO registrar(AnamnesisRequestDTO requestDTO) {
        log.info("Registrando anamnesis para paciente con cédula: {}", requestDTO.getCedulaPaciente());

        // 1. RESOLVER PACIENTE POR CÉDULA (y validar activo en SGBITAMEDICA03)
        PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(requestDTO.getCedulaPaciente());
        if (Boolean.FALSE.equals(paciente.getActivo())) {
            throw new IllegalArgumentException(
                    "El paciente con cédula " + requestDTO.getCedulaPaciente() + " no está activo en SGBITAMEDICA03");
        }
        String pacienteId = String.valueOf(paciente.getId());

        // 2. RESOLVER MÉDICO POR CÉDULA (y validar activo en SGBITAMEDICA03)
        MedicoExternoDTO medico = sgbClient.obtenerMedicoPorCedula(requestDTO.getCedulaMedico());
        if (Boolean.FALSE.equals(medico.getActivo())) {
            throw new IllegalArgumentException(
                    "El médico con cédula " + requestDTO.getCedulaMedico() + " no está activo en SGBITAMEDICA03");
        }
        String medicoId = String.valueOf(medico.getId());

        // 3. RECUPERAR HC DEL PACIENTE (almacenada en MongoDB local)
        HistoriaClinica historiaClinica = historiaClinicaRepository
                .findByPacienteId(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Historia Clínica no encontrada para el paciente con ID: " + pacienteId
                        + ". Asegúrese de que el paciente fue registrado en este sistema."));

        // 4. VERIFICAR QUE NO EXISTA UNA ANAMNESIS PREVIA PARA ESTA HC - ACTUALIZANDO
        //if (anamnesisRepository.existsByHistoriaClinicaId(historiaClinica.getId())) {
        //    throw new IllegalArgumentException(
        //            "Ya existe una anamnesis registrada para la Historia Clínica: "
        //            + historiaClinica.getNumeroHistoriaClinica()
        //            + ". Utilice el endpoint de actualización si desea modificarla.");
        //}

        // 5. DTO → ENTITY (pacienteId/medicoId ya resueltos se setean manualmente)
        Anamnesis anamnesis = anamnesisMapper.toEntity(requestDTO);
        anamnesis.setPacienteId(pacienteId);
        anamnesis.setMedicoId(medicoId);
        anamnesis.setHistoriaClinicaId(historiaClinica.getId());

        // 6. PERSISTIR EN MONGODB
        Anamnesis guardada = anamnesisRepository.save(anamnesis);
        log.info("Anamnesis registrada con ID: {}", guardada.getId());

        // 7. ENTITY → RESPONSE enriquecido
        AnamnesisResponseDTO response = anamnesisMapper.toDTO(guardada);
        completarDatosResponse(response, paciente, medico);

        return response;
    }

    @Override
    public List<AnamnesisResponseDTO> listarTodas() {
        return anamnesisRepository.findAll().stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public AnamnesisResponseDTO obtenerPorId(String id) {
        Anamnesis anamnesis = buscarPorId(id);
        AnamnesisResponseDTO response = anamnesisMapper.toDTO(anamnesis);
        completarDatosResponseDesdeIds(response);
        return response;
    }

    @Override
    public AnamnesisResponseDTO actualizar(String id, AnamnesisUpdateRequestDTO requestDTO) {
        log.info("Actualizando anamnesis ID: {}", id);
        Anamnesis anamnesis = buscarPorId(id);
        anamnesisMapper.updateEntity(requestDTO, anamnesis);
        Anamnesis actualizada = anamnesisRepository.save(anamnesis);
        log.info("Anamnesis ID: {} actualizada correctamente", actualizada.getId());
        AnamnesisResponseDTO response = anamnesisMapper.toDTO(actualizada);
        completarDatosResponseDesdeIds(response);
        return response;
    }

    @Override
    public void eliminar(String id) {
        log.info("Eliminando anamnesis ID: {}", id);
        buscarPorId(id);
        anamnesisRepository.deleteById(id);
        log.info("Anamnesis ID: {} eliminada correctamente", id);
    }

    // ── OTRAS CONSULTAS ───────────────────────────────────────────────────────

    @Override
    public List<AnamnesisResponseDTO> obtenerPorHistoriaClinica(String historiaClinicaId) {
        return anamnesisRepository.findByHistoriaClinicaId(historiaClinicaId)
                .stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public List<AnamnesisResponseDTO> listarPorPaciente(String pacienteId) {
        return anamnesisRepository.findByPacienteId(pacienteId).stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public List<AnamnesisResponseDTO> listarPorMedico(String medicoId) {
        return anamnesisRepository.findByMedicoId(medicoId).stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public List<AnamnesisResponseDTO> listarPorPacienteCedula(String cedula) {
        PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(cedula);
        return listarPorPaciente(String.valueOf(paciente.getId()));
    }

    @Override
    public List<AnamnesisResponseDTO> listarPorMedicoCedula(String cedula) {
        MedicoExternoDTO medico = sgbClient.obtenerMedicoPorCedula(cedula);
        return listarPorMedico(String.valueOf(medico.getId()));
    }

    // ── Métodos privados ──────────────────────────────────────────────────────

    private Anamnesis buscarPorId(String id) {
        return anamnesisRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Anamnesis no encontrada con ID: " + id));
    }

    private AnamnesisResponseDTO toResponseCompleto(Anamnesis anamnesis) {
        AnamnesisResponseDTO dto = anamnesisMapper.toDTO(anamnesis);
        completarDatosResponseDesdeIds(dto);
        return dto;
    }

    /** Completa el response cuando ya tenemos los objetos en memoria. */
    private void completarDatosResponse(AnamnesisResponseDTO response,
                                        PacienteExternoDTO paciente,
                                        MedicoExternoDTO medico) {
        response.setNombrePaciente(paciente.getNombreCompleto());
        response.setCedulaPaciente(paciente.getCedula());
        response.setNombreMedico(medico.getNombreCompleto());
    }

    /**
     * Completa el response consultando SGBITAMEDICA03 con los IDs del DTO.
     * Si el sistema externo no responde, los campos quedan vacíos (no falla la consulta local).
     */
    private void completarDatosResponseDesdeIds(AnamnesisResponseDTO response) {
        try {
            PacienteExternoDTO paciente = sgbClient.obtenerPacientePorId(response.getPacienteId());
            response.setNombrePaciente(paciente.getNombreCompleto());
            response.setCedulaPaciente(paciente.getCedula());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos de paciente ID {}: {}", response.getPacienteId(), e.getMessage());
        }
        try {
            MedicoExternoDTO medico = sgbClient.obtenerMedicoPorId(response.getMedicoId());
            response.setNombreMedico(medico.getNombreCompleto());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos de médico ID {}: {}", response.getMedicoId(), e.getMessage());
        }
    }
}
