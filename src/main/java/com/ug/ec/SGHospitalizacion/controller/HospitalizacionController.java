package com.ug.ec.SGHospitalizacion.controller;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoHospitalizacion;
import com.ug.ec.SGHospitalizacion.dto.external.MedicoExternoDTO;
import com.ug.ec.SGHospitalizacion.dto.request.HospitalizacionRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.HospitalizacionUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.HospitalizacionResponseDTO;
import com.ug.ec.SGHospitalizacion.interfaces.IHospitalizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospitalizaciones")
@RequiredArgsConstructor
@Tag(
        name = "Hospitalizaciones",
        description = "Gestión de ingresos hospitalarios. Solo un médico activo puede registrar una hospitalización."
)

public class HospitalizacionController {
    private final IHospitalizacionService hospitalizacionService;

    // ── POST ─────────────────────────────────────────────────────────────────

    @Operation(summary = "Registrar una nueva hospitalización")
    @PostMapping
    public ResponseEntity<HospitalizacionResponseDTO> registrar(
            @Valid @RequestBody HospitalizacionRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(hospitalizacionService.registrar(requestDTO));
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    @Operation(summary = "Obtener hospitalización por ID")
    @GetMapping("/{id}")
    public ResponseEntity<HospitalizacionResponseDTO> obtenerPorId(
            @Parameter(description = "ID MongoDB de la hospitalización")
            @PathVariable String id) {
        return ResponseEntity.ok(hospitalizacionService.obtenerPorId(id));
    }

    @Operation(summary = "Obtener hospitalización por número de ingreso")
    @GetMapping("/numero/{numeroIngreso}")
    public ResponseEntity<HospitalizacionResponseDTO> obtenerPorNumeroIngreso(
            @Parameter(description = "Número de ingreso, ej: HOSP-000001")
            @PathVariable String numeroIngreso) {
        return ResponseEntity.ok(hospitalizacionService.obtenerPorNumeroIngreso(numeroIngreso));
    }

    @Operation(summary = "Listar todas las hospitalizaciones")
    @GetMapping
    public ResponseEntity<List<HospitalizacionResponseDTO>> listarTodas() {
        return ResponseEntity.ok(hospitalizacionService.listarTodas());
    }

    @Operation(summary = "Listar hospitalizaciones por estado (ACTIVO, ALTA, TRASLADADO, FALLECIDO)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<HospitalizacionResponseDTO>> listarPorEstado(
            @Parameter(description = "EstadoHospitalizacion: ACTIVO | ALTA | TRASLADADO | FALLECIDO")
            @PathVariable EstadoHospitalizacion estado) {
        return ResponseEntity.ok(hospitalizacionService.listarPorEstado(estado));
    }

    @Operation(summary = "Listar hospitalizaciones de un paciente")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<HospitalizacionResponseDTO>> listarPorPaciente(
            @Parameter(description = "ID MongoDB del paciente")
            @PathVariable String pacienteId) {
        return ResponseEntity.ok(hospitalizacionService.listarPorPaciente(pacienteId));
    }

    @Operation(summary = "Listar hospitalizaciones de un paciente por cédula",
               description = "Consulta la cédula en SGBITAMEDICA03, obtiene el ID interno del paciente y busca sus hospitalizaciones.")
    @GetMapping("/paciente/cedula/{cedula}")
    public ResponseEntity<List<HospitalizacionResponseDTO>> listarPorPacienteCedula(
            @Parameter(description = "Cédula del paciente")
            @PathVariable String cedula) {
        return ResponseEntity.ok(hospitalizacionService.listarPorPacienteCedula(cedula));
    }

    @Operation(summary = "Listar hospitalizaciones registradas por un médico")
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<HospitalizacionResponseDTO>> listarPorMedico(
            @Parameter(description = "ID MongoDB del médico")
            @PathVariable String medicoId) {
        return ResponseEntity.ok(hospitalizacionService.listarPorMedico(medicoId));
    }

    @Operation(summary = "Listar hospitalizaciones registradas por un médico, por cédula",
               description = "Consulta la cédula en SGBITAMEDICA03, obtiene el ID interno del médico y busca sus hospitalizaciones.")
    @GetMapping("/medico/cedula/{cedula}")
    public ResponseEntity<List<HospitalizacionResponseDTO>> listarPorMedicoCedula(
            @Parameter(description = "Cédula del médico")
            @PathVariable String cedula) {
        return ResponseEntity.ok(hospitalizacionService.listarPorMedicoCedula(cedula));
    }

    @GetMapping("/medicos")
    public ResponseEntity<List<MedicoExternoDTO>> listarMedicos() {
        return ResponseEntity.ok(hospitalizacionService.listarMedicos());
    }

    // ── PUT ──────────────────────────────────────────────────────────────────

    @Operation(summary = "Actualizar hospitalización (estado, egreso, sala, diagnóstico)")
    @PutMapping("/{id}")
    public ResponseEntity<HospitalizacionResponseDTO> actualizar(
            @Parameter(description = "ID MongoDB de la hospitalización")
            @PathVariable String id,
            @Valid @RequestBody HospitalizacionUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(hospitalizacionService.actualizar(id, requestDTO));
    }
}
