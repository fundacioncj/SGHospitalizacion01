package com.ug.ec.SGHospitalizacion.controller;

import com.ug.ec.SGHospitalizacion.dto.request.AnamnesisRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.AnamnesisUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.AnamnesisResponseDTO;
import com.ug.ec.SGHospitalizacion.interfaces.IAnamnesisService;
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
@RequestMapping("/api/v1/anamnesis")
@RequiredArgsConstructor
@Tag(name = "Anamnesis", description = "Gestión de anamnesis del paciente hospitalizado." )

public class AnamnesisController {

    private final IAnamnesisService anamnesisService;

    // * POST ----------------------------------------------------------------------------------------------------------

    @Operation(summary = "Registrar anamnesis del paciente (PB-07)")
    @PostMapping
    public ResponseEntity<AnamnesisResponseDTO> registrar(@Valid @RequestBody AnamnesisRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(anamnesisService.registrar(requestDTO));
    }

    // * GET ----------------------------------------------------------------------------------------------------------

    // ? Obtener todas las anamnesis

    @Operation(summary = "Listar todas las anamnesis")
    @GetMapping
    public ResponseEntity<List<AnamnesisResponseDTO>> listarTodas() {
        return ResponseEntity.ok(anamnesisService.listarTodas());
    }

    // ? Obtener anamnesis por ID
    @GetMapping("/{id}")
    public ResponseEntity<AnamnesisResponseDTO> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(anamnesisService.obtenerPorId(id));
    }

    // ? Obtener anamnesis por historia clínica - ACTUALIZANDOO
    @GetMapping("/historia-clinica/{historiaClinicaId}")
    public ResponseEntity<List<AnamnesisResponseDTO>> obtenerPorHistoriaClinica(@PathVariable String historiaClinicaId) {
        return ResponseEntity.ok(anamnesisService.obtenerPorHistoriaClinica(historiaClinicaId)
        );
    }

    // ? Listar anamnesis de un paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<AnamnesisResponseDTO>> listarPorPaciente(@PathVariable String pacienteId) {
        return ResponseEntity.ok(anamnesisService.listarPorPaciente(pacienteId));
    }

    // ? Listar anamnesis de un paciente por cédula
    @GetMapping("/paciente/cedula/{cedula}")
    public ResponseEntity<List<AnamnesisResponseDTO>> listarPorPacienteCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(anamnesisService.listarPorPacienteCedula(cedula));
    }

    // ? Listar anamnesis registradas por un médico
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AnamnesisResponseDTO>> listarPorMedico(@PathVariable String medicoId) {
        return ResponseEntity.ok(anamnesisService.listarPorMedico(medicoId));
    }

    // ? Listar anamnesis registradas por un médico, por cédula
    @GetMapping("/medico/cedula/{cedula}")
    public ResponseEntity<List<AnamnesisResponseDTO>> listarPorMedicoCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(anamnesisService.listarPorMedicoCedula(cedula));
    }

    // * PUT ----------------------------------------------------------------------------------------------------------

    // ? Editar anamnesis para corregir información clínica
    @PutMapping("/{id}")
    public ResponseEntity<AnamnesisResponseDTO> actualizar(@PathVariable String id,
            @Valid @RequestBody AnamnesisUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(anamnesisService.actualizar(id, requestDTO));
    }

    // * DELETE -------------------------------------------------------------------------------------------------------

    // ? Eliminar anamnesis por ID"
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        anamnesisService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
