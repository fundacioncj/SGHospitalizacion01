package com.ug.ec.SGHospitalizacion.infrastructure.client;

import com.ug.ec.SGHospitalizacion.dto.external.MedicoExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.external.PacienteExternoDTO;
import com.ug.ec.SGHospitalizacion.exception.IntegracionExternaException;
import com.ug.ec.SGHospitalizacion.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SgbCitaMedicaClient {

    private final RestTemplate restTemplate;

    @Qualifier("sgbBaseUrl")
    private final String baseUrl;

    // ── PACIENTES ─────────────────────────────────────────────────────────────

    public PacienteExternoDTO obtenerPacientePorId(String pacienteId) {
        String url = baseUrl + "/api/pacientes/" + pacienteId;
        log.debug("Consultando paciente en SGBITAMEDICA03: {}", url);
        try {
            ResponseEntity<PacienteExternoDTO> response =
                    restTemplate.getForEntity(url, PacienteExternoDTO.class);
            log.info("RESPUESTA PACIENTE SGB: {}", response.getBody()); // aqui prueba
            return validarRespuesta(response, "Paciente", pacienteId);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RecursoNoEncontradoException(
                        "Paciente con ID " + pacienteId + " no encontrado en SGBITAMEDICA03");
            }
            throw new IntegracionExternaException(
                    "Error al consultar paciente en SGBITAMEDICA03: " + ex.getMessage(), ex);
        } catch (ResourceAccessException ex) {
            throw new IntegracionExternaException(
                    "SGBITAMEDICA03 no está disponible. Verifique que el sistema base esté activo.", ex);
        }
    }

    public PacienteExternoDTO obtenerPacienteActivoPorId(String pacienteId) {
        PacienteExternoDTO paciente = obtenerPacientePorId(pacienteId);
        if (Boolean.FALSE.equals(paciente.getActivo())) {
            throw new IllegalArgumentException(
                    "El paciente con ID " + pacienteId + " no está activo en SGBITAMEDICA03");
        }
        return paciente;
    }

    public PacienteExternoDTO obtenerPacientePorCedula(String cedula) {
        String url = baseUrl + "/api/pacientes/cedula/" + cedula;
        log.debug("Consultando paciente por cédula en SGBITAMEDICA03: {}", url);
        try {
            ResponseEntity<PacienteExternoDTO> response =
                    restTemplate.getForEntity(url, PacienteExternoDTO.class);
            return validarRespuesta(response, "Paciente", cedula);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RecursoNoEncontradoException(
                        "Paciente con cédula " + cedula + " no encontrado en SGBITAMEDICA03");
            }
            throw new IntegracionExternaException(
                    "Error al consultar paciente por cédula en SGBITAMEDICA03: " + ex.getMessage(), ex);
        } catch (ResourceAccessException ex) {
            throw new IntegracionExternaException(
                    "SGBITAMEDICA03 no está disponible. Verifique que el sistema base esté activo.", ex);
        }
    }

    // ── MÉDICOS ───────────────────────────────────────────────────────────────

    public MedicoExternoDTO obtenerMedicoPorId(String medicoId) {
        String url = baseUrl + "/api/medicos/" + medicoId;
        log.debug("Consultando médico en SGBITAMEDICA03: {}", url);
        try {
            ResponseEntity<MedicoExternoDTO> response =
                    restTemplate.getForEntity(url, MedicoExternoDTO.class);
            return validarRespuesta(response, "Médico", medicoId);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RecursoNoEncontradoException(
                        "Médico con ID " + medicoId + " no encontrado en SGBITAMEDICA03");
            }
            throw new IntegracionExternaException(
                    "Error al consultar médico en SGBITAMEDICA03: " + ex.getMessage(), ex);
        } catch (ResourceAccessException ex) {
            throw new IntegracionExternaException(
                    "SGBITAMEDICA03 no está disponible. Verifique que el sistema base esté activo.", ex);
        }
    }

    public MedicoExternoDTO obtenerMedicoActivoPorId(String medicoId) {
        MedicoExternoDTO medico = obtenerMedicoPorId(medicoId);
        if (Boolean.FALSE.equals(medico.getActivo())) {
            throw new IllegalArgumentException(
                    "El médico con ID " + medicoId + " no está activo en SGBITAMEDICA03");
        }
        return medico;
    }

    public MedicoExternoDTO obtenerMedicoPorCedula(String cedula) {
        String url = baseUrl + "/api/medicos/cedula/" + cedula;
        log.debug("Consultando médico por cédula en SGBITAMEDICA03: {}", url);
        try {
            ResponseEntity<MedicoExternoDTO> response =
                    restTemplate.getForEntity(url, MedicoExternoDTO.class);
            return validarRespuesta(response, "Médico", cedula);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RecursoNoEncontradoException(
                        "Médico con cédula " + cedula + " no encontrado en SGBITAMEDICA03");
            }
            throw new IntegracionExternaException(
                    "Error al consultar médico por cédula en SGBITAMEDICA03: " + ex.getMessage(), ex);
        } catch (ResourceAccessException ex) {
            throw new IntegracionExternaException(
                    "SGBITAMEDICA03 no está disponible. Verifique que el sistema base esté activo.", ex);
        }
    }

    // Obtener Medicos Activos

    public List<MedicoExternoDTO> obtenerMedicos() {
        String url = baseUrl + "/api/medicos";

        ResponseEntity<MedicoExternoDTO[]> response = restTemplate.getForEntity(url, MedicoExternoDTO[].class);

        MedicoExternoDTO[] medicos = validarRespuesta(response, "Médicos", "lista");

        return Arrays.asList(medicos);
    }

    // ── Métodos privados ──────────────────────────────────────────────────────

    private <T> T validarRespuesta(ResponseEntity<T> response, String entidad, String id) {
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        throw new RecursoNoEncontradoException(
                entidad + " con ID " + id + " no encontrado en SGBITAMEDICA03");
    }
}