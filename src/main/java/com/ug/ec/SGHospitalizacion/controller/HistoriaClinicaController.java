package com.ug.ec.SGHospitalizacion.controller;

import com.ug.ec.SGHospitalizacion.dto.request.HistoriaClinicaUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.HistoriaClinicaResponseDTO;
import com.ug.ec.SGHospitalizacion.interfaces.IHistoriaClinicaService;
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
@RequestMapping("/api/v1/historias-clinicas")
@RequiredArgsConstructor
@Tag(name = "Historias Clínicas",
     description = "Gestión de Historias Clínicas. El pacienteId referencia al ID SQL de SGBITAMEDICA03.")
public class HistoriaClinicaController {

    private final IHistoriaClinicaService historiaClinicaService;

    @Operation(summary = "Abrir Historia Clínica para un paciente de SGBITAMEDICA03",
               description = "Recibe el ID SQL del paciente. Si ya existe una HC, la devuelve.")
    @PostMapping("/abrir/{pacienteId}")
    public ResponseEntity<HistoriaClinicaResponseDTO> abrir(
            @Parameter(description = "ID SQL del paciente en SGBITAMEDICA03")
            @PathVariable String pacienteId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(historiaClinicaService.abrirParaPaciente(pacienteId));
    }

    @Operation(summary = "Abrir Historia Clínica para un paciente por cédula",
               description = "Consulta la cédula en SGBITAMEDICA03, obtiene el ID interno del paciente y abre (o devuelve) su HC.")
    @PostMapping("/abrir/cedula/{cedula}")
    public ResponseEntity<HistoriaClinicaResponseDTO> abrirPorCedula(
            @Parameter(description = "Cédula del paciente")
            @PathVariable String cedula) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(historiaClinicaService.abrirParaPacienteCedula(cedula));
    }

    @Operation(summary = "Listar todas las historias clínicas")
    @GetMapping
    public ResponseEntity<List<HistoriaClinicaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(historiaClinicaService.listarTodas());
    }

    @Operation(summary = "Obtener Historia Clínica por su ID MongoDB")
    @GetMapping("/{id}")
    public ResponseEntity<HistoriaClinicaResponseDTO> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorId(id));
    }

    @Operation(summary = "Obtener Historia Clínica por ID de paciente (SQL)",
               description = "El pacienteId es el ID SQL del paciente en SGBITAMEDICA03.")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<HistoriaClinicaResponseDTO> obtenerPorPaciente(
            @Parameter(description = "ID SQL del paciente en SGBITAMEDICA03")
            @PathVariable String pacienteId) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorPacienteId(pacienteId));
    }

    @Operation(summary = "Obtener Historia Clínica por cédula del paciente",
               description = "Consulta la cédula en SGBITAMEDICA03, obtiene el ID interno del paciente y busca su Historia Clínica.")
    @GetMapping("/paciente/cedula/{cedula}")
    public ResponseEntity<HistoriaClinicaResponseDTO> obtenerPorPacienteCedula(
            @Parameter(description = "Cédula del paciente")
            @PathVariable String cedula) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorCedulaPaciente(cedula));
    }

    @Operation(summary = "Actualizar antecedentes / observaciones de la Historia Clínica")
    @PutMapping("/{id}")
    public ResponseEntity<HistoriaClinicaResponseDTO> actualizar(
            @PathVariable String id,
            @Valid @RequestBody HistoriaClinicaUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(historiaClinicaService.actualizar(id, requestDTO));
    }
}
