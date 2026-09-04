package com.ug.ec.SGHospitalizacion.service;

import com.ug.ec.SGHospitalizacion.dao.HospitalizacionRepository;
import com.ug.ec.SGHospitalizacion.domain.Hospitalizacion;
import com.ug.ec.SGHospitalizacion.domain.enums.EstadoHospitalizacion;
import com.ug.ec.SGHospitalizacion.dto.external.MedicoExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.external.PacienteExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.request.HospitalizacionRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.HospitalizacionUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.HospitalizacionResponseDTO;
import com.ug.ec.SGHospitalizacion.infrastructure.client.SgbCitaMedicaClient;
import com.ug.ec.SGHospitalizacion.interfaces.IHospitalizacionService;
import com.ug.ec.SGHospitalizacion.mapper.HospitalizacionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de Hospitalización refactorizado para DDD con integración REST a SGBITAMEDICA03.
 *
 * - pacienteId y medicoId son IDs SQL del sistema base (SGBITAMEDICA03).
 * - La validación de existencia y estado activo se delega al SgbCitaMedicaClient.
 * - Los datos de nombre/cédula se enriquecen consultando el cliente externo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalizacionServiceImpl implements IHospitalizacionService {

    private final HospitalizacionRepository hospitalizacionRepository;
    private final HospitalizacionMapper      hospitalizacionMapper;
    private final SgbCitaMedicaClient        sgbClient;            // ← integración REST

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @Override
    public HospitalizacionResponseDTO registrar(HospitalizacionRequestDTO requestDTO) {
        log.info("Registrando hospitalización para paciente con cédula: {}", requestDTO.getCedulaPaciente());

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

        // 3. EVITAR DOBLE HOSPITALIZACIÓN ACTIVA
        if (hospitalizacionRepository.existsByPacienteIdAndEstado(
                pacienteId, EstadoHospitalizacion.ACTIVO)) {
            throw new IllegalArgumentException(
                    "El paciente ya tiene una hospitalización activa. Egrese la actual antes de registrar una nueva.");
        }

        // 4. DTO → ENTITY (pacienteId/medicoId ya resueltos se setean manualmente)
        Hospitalizacion hosp = hospitalizacionMapper.toEntity(requestDTO);
        hosp.setPacienteId(pacienteId);
        hosp.setMedicoId(medicoId);
        hosp.setNumeroIngreso(generarNumeroIngreso());
        hosp.setEstado(EstadoHospitalizacion.ACTIVO);

        // 5. PERSISTIR EN MONGODB
        Hospitalizacion guardada = hospitalizacionRepository.save(hosp);
        log.info("Hospitalización {} registrada correctamente", guardada.getNumeroIngreso());

        // 6. ENTITY → RESPONSE (enriquecido con datos del sistema base)
        HospitalizacionResponseDTO response = hospitalizacionMapper.toDTO(guardada);
        enriquecerResponse(response, paciente, medico);

        return response;
    }

    @Override
    public HospitalizacionResponseDTO obtenerPorId(String id) {
        Hospitalizacion hosp = buscarPorId(id);
        HospitalizacionResponseDTO response = hospitalizacionMapper.toDTO(hosp);
        enriquecerResponseDesdeIds(response);
        return response;
    }

    @Override
    public HospitalizacionResponseDTO obtenerPorNumeroIngreso(String numeroIngreso) {
        Hospitalizacion hosp = hospitalizacionRepository.findByNumeroIngreso(numeroIngreso)
                .orElseThrow(() -> new com.ug.ec.SGHospitalizacion.exception.RecursoNoEncontradoException(
                        "Hospitalización no encontrada con número: " + numeroIngreso));
        HospitalizacionResponseDTO response = hospitalizacionMapper.toDTO(hosp);
        enriquecerResponseDesdeIds(response);
        return response;
    }

    @Override
    public List<HospitalizacionResponseDTO> listarTodas() {
        return hospitalizacionRepository.findAll().stream()
                .map(this::toResponseEnriquecido)
                .toList();
    }

    @Override
    public List<HospitalizacionResponseDTO> listarPorEstado(EstadoHospitalizacion estado) {
        return hospitalizacionRepository.findByEstado(estado).stream()
                .map(this::toResponseEnriquecido)
                .toList();
    }

    @Override
    public List<HospitalizacionResponseDTO> listarPorPaciente(String pacienteId) {
        return hospitalizacionRepository.findByPacienteId(pacienteId).stream()
                .map(this::toResponseEnriquecido)
                .toList();
    }

    @Override
    public List<HospitalizacionResponseDTO> listarPorMedico(String medicoId) {
        return hospitalizacionRepository.findByMedicoId(medicoId).stream()
                .map(this::toResponseEnriquecido)
                .toList();
    }

    @Override
    public List<HospitalizacionResponseDTO> listarPorPacienteCedula(String cedula) {
        PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(cedula);
        return listarPorPaciente(String.valueOf(paciente.getId()));
    }

    @Override
    public List<HospitalizacionResponseDTO> listarPorMedicoCedula(String cedula) {
        MedicoExternoDTO medico = sgbClient.obtenerMedicoPorCedula(cedula);
        return listarPorMedico(String.valueOf(medico.getId()));
    }

    @Override
    public List<MedicoExternoDTO> listarMedicos() {
        return sgbClient.obtenerMedicos();
    }

    @Override
    public HospitalizacionResponseDTO actualizar(String id, HospitalizacionUpdateRequestDTO requestDTO) {
        log.info("Actualizando hospitalización ID: {}", id);
        Hospitalizacion hosp = buscarPorId(id);
        hospitalizacionMapper.updateEntity(requestDTO, hosp);
        Hospitalizacion actualizada = hospitalizacionRepository.save(hosp);
        HospitalizacionResponseDTO response = hospitalizacionMapper.toDTO(actualizada);
        enriquecerResponseDesdeIds(response);
        return response;
    }

    // ── Métodos privados ──────────────────────────────────────────────────────

    private String generarNumeroIngreso() { //Actualizado
        Optional<Hospitalizacion> ultima = hospitalizacionRepository.findTopByOrderByNumeroIngresoDesc();

        long siguiente = 1;

        if (ultima.isPresent() && ultima.get().getNumeroIngreso() != null) {
            String numero = ultima.get().getNumeroIngreso().replace("HOSP-", "");
            siguiente = Long.parseLong(numero) + 1;
        }
        return "HOSP-" + String.format("%06d", siguiente);
    }

    private Hospitalizacion buscarPorId(String id) {
        return hospitalizacionRepository.findById(id)
                .orElseThrow(() -> new com.ug.ec.SGHospitalizacion.exception.RecursoNoEncontradoException(
                        "Hospitalización no encontrada con ID: " + id));
    }

    private HospitalizacionResponseDTO toResponseEnriquecido(Hospitalizacion hosp) {
        HospitalizacionResponseDTO dto = hospitalizacionMapper.toDTO(hosp);
        enriquecerResponseDesdeIds(dto);
        System.out.println("PacienteId = " + hosp.getPacienteId()); /*probando..*/
        System.out.println("MedicoId   = " + hosp.getMedicoId());
        return dto;
    }

    /**
     * Enriquece con los objetos ya en memoria (evita llamadas REST extra al registrar).
     */
    private void enriquecerResponse(HospitalizacionResponseDTO response,
                                    PacienteExternoDTO paciente,
                                    MedicoExternoDTO medico) {
        response.setNombrePaciente(paciente.getNombreCompleto());
        response.setCedulaPaciente(paciente.getCedula());
        response.setNombreMedico(medico.getNombreCompleto());
    }

    /**
     * Enriquece consultando SGBITAMEDICA03 con los IDs ya presentes en el DTO.
     * Si el sistema externo no responde, los campos quedan con su ID como valor.
     */
    private void enriquecerResponseDesdeIds(HospitalizacionResponseDTO response) {
        try {
            PacienteExternoDTO paciente = sgbClient.obtenerPacientePorId(response.getPacienteId());

            System.out.println("Paciente recibido:");
            System.out.println("ID: " + paciente.getId());
            System.out.println("Cedula: " + paciente.getCedula());
            System.out.println("Nombre: " + paciente.getNombreCompleto());

            response.setNombrePaciente(paciente.getNombreCompleto());
            response.setCedulaPaciente(paciente.getCedula());
        } catch (Exception e) {
            log.warn("No se pudo enriquecer datos de paciente ID {}: {}", response.getPacienteId(), e.getMessage());
        }
        try {
            MedicoExternoDTO medico = sgbClient.obtenerMedicoPorId(response.getMedicoId());

            System.out.println("Medico recibido:");
            System.out.println("ID: " + medico.getId());
            System.out.println("Nombre: " + medico.getNombreCompleto());

            response.setNombreMedico(medico.getNombreCompleto());
        } catch (Exception e) {
            log.warn("No se pudo enriquecer datos de médico ID {}: {}", response.getMedicoId(), e.getMessage());
        }
    }
}
