package com.ug.ec.SGHospitalizacion.controller;

import com.ug.ec.SGHospitalizacion.dto.request.ExamenMedicoRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.ExamenMedicoUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.ExamenMedicoResponseDTO;
import com.ug.ec.SGHospitalizacion.interfaces.IExamenMedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/examenes-medicos")
@RequiredArgsConstructor
public class ExamenMedicoController {

    private final IExamenMedicoService examenMedicoService;

    // Registrar examen médico
    @PostMapping
    public ResponseEntity<ExamenMedicoResponseDTO> registrar(@Valid @RequestBody ExamenMedicoRequestDTO requestDTO) {
        return ResponseEntity.ok(examenMedicoService.registrar(requestDTO));
    }

    // Obtener examen médico por ID
    @GetMapping("/{id}")
    public ResponseEntity<ExamenMedicoResponseDTO> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(examenMedicoService.obtenerPorId(id));
    }

    // Actualizar examen médico
    @PutMapping("/{id}")
    public ResponseEntity<ExamenMedicoResponseDTO> actualizar(@PathVariable String id, @RequestBody ExamenMedicoUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(examenMedicoService.actualizar(id, requestDTO));
    }

    // Eliminar examen médico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        examenMedicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los exámenes médicos
    @GetMapping
    public ResponseEntity<List<ExamenMedicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(examenMedicoService.listarTodos());
    }

    // Listar por historia clínica ID
    @GetMapping("/historia-clinica/{historiaClinicaId}")
    public ResponseEntity<List<ExamenMedicoResponseDTO>> listarPorHistoriaClinica(@PathVariable String historiaClinicaId) {
        return ResponseEntity.ok(examenMedicoService.listarPorHistoriaClinicaId(historiaClinicaId));
    }

    // Listar por número de historia clínica
    @GetMapping("/numero-historia-clinica/{numeroHistoriaClinica}")
    public ResponseEntity<List<ExamenMedicoResponseDTO>> listarPorNumeroHistoriaClinica(@PathVariable String numeroHistoriaClinica) {
        return ResponseEntity.ok(examenMedicoService.listarPorNumeroHistoriaClinica(numeroHistoriaClinica));
    }

    // Listar por paciente ID
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ExamenMedicoResponseDTO>> listarPorPaciente(@PathVariable String pacienteId) {
        return ResponseEntity.ok(examenMedicoService.listarPorPacienteId(pacienteId));
    }

    // Listar por paciente mediante cédula
    @GetMapping("/paciente/cedula/{cedula}")
    public ResponseEntity<List<ExamenMedicoResponseDTO>> listarPorPacienteCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(examenMedicoService.listarPorPacienteCedula(cedula));
    }

    // Listar por paciente mediante cédula y número HC
    @GetMapping("/paciente/cedula/{cedula}/historia/{numeroHistoriaClinica}")
    public ResponseEntity<List<ExamenMedicoResponseDTO>> listarPorPacienteCedulaYNumeroHistoria(
            @PathVariable String cedula,
            @PathVariable String numeroHistoriaClinica) {
        return ResponseEntity.ok(examenMedicoService.listarPorPacienteCedulaYNumeroHistoria(cedula, numeroHistoriaClinica));
    }

    // Listar por médico ID
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<ExamenMedicoResponseDTO>> listarPorMedico(@PathVariable String medicoId) {
        return ResponseEntity.ok(examenMedicoService.listarPorMedicoId(medicoId));
    }

    // Listar por médico mediante cédula
    @GetMapping("/medico/cedula/{cedula}")
    public ResponseEntity<List<ExamenMedicoResponseDTO>> listarPorMedicoCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(examenMedicoService.listarPorMedicoCedula(cedula));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generarPdf(@PathVariable String id) {
        byte[] pdf = examenMedicoService.generarPdf(id);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header(
                        "Content-Disposition",
                        "attachment; filename=\"HCU-003-" + id + ".pdf\""
                )
                .body(pdf);
    }
}