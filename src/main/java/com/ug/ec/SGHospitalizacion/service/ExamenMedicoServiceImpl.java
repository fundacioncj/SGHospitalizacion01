package com.ug.ec.SGHospitalizacion.service;

import com.ug.ec.SGHospitalizacion.dao.ExamenMedicoRepository;
import com.ug.ec.SGHospitalizacion.dao.HistoriaClinicaRepository;
import com.ug.ec.SGHospitalizacion.domain.ExamenMedico;
import com.ug.ec.SGHospitalizacion.domain.HistoriaClinica;
import com.ug.ec.SGHospitalizacion.dto.external.MedicoExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.external.PacienteExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.request.ExamenMedicoRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.ExamenMedicoUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.ExamenMedicoResponseDTO;
import com.ug.ec.SGHospitalizacion.exception.RecursoNoEncontradoException;
import com.ug.ec.SGHospitalizacion.infrastructure.client.SgbCitaMedicaClient;
import com.ug.ec.SGHospitalizacion.interfaces.IExamenMedicoService;
import com.ug.ec.SGHospitalizacion.mapper.ExamenMedicoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamenMedicoServiceImpl implements IExamenMedicoService {

    private final ExamenMedicoRepository examenMedicoRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final ExamenMedicoMapper examenMedicoMapper;
    private final SgbCitaMedicaClient sgbClient;

    @Override
    public ExamenMedicoResponseDTO registrar(ExamenMedicoRequestDTO requestDTO) {
        log.info("Registrando examen médico para paciente: {}", requestDTO.getCedulaPaciente());

        PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(requestDTO.getCedulaPaciente());
        if (Boolean.FALSE.equals(paciente.getActivo()))
            throw new IllegalArgumentException("Paciente inactivo en SGBITAMEDICA03");

        MedicoExternoDTO medico = sgbClient.obtenerMedicoPorCedula(requestDTO.getCedulaMedico());
        if (Boolean.FALSE.equals(medico.getActivo()))
            throw new IllegalArgumentException("Médico inactivo en SGBITAMEDICA03");

        HistoriaClinica historiaClinica = historiaClinicaRepository
                .findByPacienteId(String.valueOf(paciente.getId()))
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El paciente no tiene historia clínica registrada"));

        ExamenMedico examenMedico = examenMedicoMapper.toEntity(requestDTO);

        examenMedico.setPacienteId(String.valueOf(paciente.getId()));
        examenMedico.setMedicoId(String.valueOf(medico.getId()));
        examenMedico.setHistoriaClinicaId(historiaClinica.getId());
        examenMedico.setNumeroHistoriaClinica(historiaClinica.getNumeroHistoriaClinica());

        ExamenMedico guardado = examenMedicoRepository.save(examenMedico);

        ExamenMedicoResponseDTO response = examenMedicoMapper.toDTO(guardado);
        completarDatosResponse(response, paciente, medico);

        return response;
    }

    @Override
    public List<ExamenMedicoResponseDTO> listarTodos() {
        return examenMedicoRepository.findAll()
                .stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public ExamenMedicoResponseDTO obtenerPorId(String id) {
        ExamenMedico examenMedico = buscarPorId(id);
        ExamenMedicoResponseDTO response = examenMedicoMapper.toDTO(examenMedico);
        completarDatosResponseDesdeIds(response);
        return response;
    }

    @Override
    public ExamenMedicoResponseDTO actualizar(String id, ExamenMedicoUpdateRequestDTO requestDTO) {
        ExamenMedico examenMedico = buscarPorId(id);

        examenMedicoMapper.updateEntity(requestDTO, examenMedico);

        ExamenMedico actualizado = examenMedicoRepository.save(examenMedico);

        ExamenMedicoResponseDTO response = examenMedicoMapper.toDTO(actualizado);
        completarDatosResponseDesdeIds(response);

        return response;
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        examenMedicoRepository.deleteById(id);
    }

    // ── CONSULTAS ───────────────────────────────────────────────────────────

    @Override
    public List<ExamenMedicoResponseDTO> listarPorHistoriaClinicaId(String historiaClinicaId) {
        return examenMedicoRepository.findByHistoriaClinicaId(historiaClinicaId)
                .stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public List<ExamenMedicoResponseDTO> listarPorNumeroHistoriaClinica(String numeroHistoriaClinica) {
        return examenMedicoRepository.findByNumeroHistoriaClinica(numeroHistoriaClinica)
                .stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public List<ExamenMedicoResponseDTO> listarPorPacienteId(String pacienteId) {
        return examenMedicoRepository.findByPacienteId(pacienteId)
                .stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public List<ExamenMedicoResponseDTO> listarPorPacienteCedula(String cedula) {
        PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(cedula);
        return listarPorPacienteId(String.valueOf(paciente.getId()));
    }

    @Override
    public List<ExamenMedicoResponseDTO> listarPorPacienteCedulaYNumeroHistoria(String cedula, String numeroHistoriaClinica) {
        PacienteExternoDTO paciente = sgbClient.obtenerPacientePorCedula(cedula);

        return examenMedicoRepository
                .findByPacienteIdAndNumeroHistoriaClinica(
                        String.valueOf(paciente.getId()),
                        numeroHistoriaClinica
                )
                .stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public List<ExamenMedicoResponseDTO> listarPorMedicoId(String medicoId) {
        return examenMedicoRepository.findByMedicoId(medicoId)
                .stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Override
    public List<ExamenMedicoResponseDTO> listarPorMedicoCedula(String cedula) {
        MedicoExternoDTO medico = sgbClient.obtenerMedicoPorCedula(cedula);
        return listarPorMedicoId(String.valueOf(medico.getId()));
    }

    // ── PRIVADOS ────────────────────────────────────────────────────────────

    private ExamenMedico buscarPorId(String id) {
        return examenMedicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Examen médico no encontrado: " + id));
    }

    private ExamenMedicoResponseDTO toResponseCompleto(ExamenMedico examenMedico) {
        ExamenMedicoResponseDTO dto = examenMedicoMapper.toDTO(examenMedico);
        completarDatosResponseDesdeIds(dto);
        return dto;
    }

    private void completarDatosResponse(ExamenMedicoResponseDTO response, PacienteExternoDTO paciente, MedicoExternoDTO medico) {
        response.setNombrePaciente(paciente.getNombreCompleto());
        response.setCedulaPaciente(paciente.getCedula());
        response.setNombreMedico(medico.getNombreCompleto());
    }

    private void completarDatosResponseDesdeIds(ExamenMedicoResponseDTO response) {
        try {
            PacienteExternoDTO paciente = sgbClient.obtenerPacientePorId(response.getPacienteId());
            response.setNombrePaciente(paciente.getNombreCompleto());
            response.setCedulaPaciente(paciente.getCedula());
        } catch (Exception e) {
            log.warn("No se pudo obtener paciente: {}", e.getMessage());
        }

        try {
            MedicoExternoDTO medico = sgbClient.obtenerMedicoPorId(response.getMedicoId());
            response.setNombreMedico(medico.getNombreCompleto());
        } catch (Exception e) {
            log.warn("No se pudo obtener médico: {}", e.getMessage());
        }
    }

    @Override
    public byte[] generarPdf(String id) {
        ExamenMedico examenMedico = buscarPorId(id);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            document.add(new com.lowagie.text.Paragraph("HCU-003"));
            document.add(new com.lowagie.text.Paragraph(
                    "Paciente: " + (examenMedico.getPacienteId() != null
                            ? examenMedico.getPacienteId()
                            : "")
            ));
            document.add(new com.lowagie.text.Paragraph(
                    "Fecha: " + (examenMedico.getFechaRegistro() != null
                            ? examenMedico.getFechaRegistro().toString()
                            : "")
            ));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            log.error("Error generando PDF del examen médico {}", id, e);
            throw new RuntimeException("No se pudo generar el PDF", e);
        }
    }
}