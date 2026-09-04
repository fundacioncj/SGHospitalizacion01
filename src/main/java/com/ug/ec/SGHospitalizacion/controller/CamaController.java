package com.ug.ec.SGHospitalizacion.controller;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoCama;
import com.ug.ec.SGHospitalizacion.dto.request.CamaEstadoUpdateRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.request.CamaRequestDTO;
import com.ug.ec.SGHospitalizacion.dto.response.CamaResponseDTO;
import com.ug.ec.SGHospitalizacion.interfaces.ICamaService;
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
@RequestMapping("/api/v1/camas")
@RequiredArgsConstructor
@Tag(
        name = "Camas",
        description = "Gestión de camas hospitalarias. Los endpoints de escritura requieren adminId con rol ADMINISTRADOR."
)

public class CamaController {
    private final ICamaService camaService;

    // ── Escritura (solo administrador) ────────────────────────────────────────

    @Operation(summary = "Registrar una nueva cama")
    @PostMapping
    public ResponseEntity<CamaResponseDTO> registrar(
            @Valid @RequestBody CamaRequestDTO requestDTO,
            @Parameter(description = "ID MongoDB del administrador") @RequestParam String adminId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(camaService.registrar(requestDTO, adminId));
    }

    @Operation(summary = "Cambiar estado de una cama (DISPONIBLE o MANTENIMIENTO)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<CamaResponseDTO> actualizarEstado(
            @Parameter(description = "ID MongoDB de la cama") @PathVariable String id,
            @Valid @RequestBody CamaEstadoUpdateRequestDTO requestDTO,
            @Parameter(description = "ID MongoDB del administrador") @RequestParam String adminId) {
        return ResponseEntity.ok(camaService.actualizarEstado(id, requestDTO, adminId));
    }

    // ── Consultas ─────────────────────────────────────────────────────────────

    @Operation(summary = "Obtener cama por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CamaResponseDTO> obtenerPorId(
            @Parameter(description = "ID MongoDB de la cama") @PathVariable String id) {
        return ResponseEntity.ok(camaService.obtenerPorId(id));
    }

    @Operation(summary = "Obtener cama por número (ej: 204-B, UCI-03)")
    @GetMapping("/numero/{numeroCama}")
    public ResponseEntity<CamaResponseDTO> obtenerPorNumeroCama(
            @Parameter(description = "Número físico de la cama") @PathVariable String numeroCama) {
        return ResponseEntity.ok(camaService.obtenerPorNumeroCama(numeroCama));
    }

    @Operation(summary = "Listar todas las camas del hospital")
    @GetMapping
    public ResponseEntity<List<CamaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(camaService.listarTodas());
    }

    @Operation(summary = "Listar camas por estado (DISPONIBLE, OCUPADA, MANTENIMIENTO)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CamaResponseDTO>> listarPorEstado(
            @Parameter(description = "EstadoCama: DISPONIBLE | OCUPADA | MANTENIMIENTO")
            @PathVariable EstadoCama estado) {
        return ResponseEntity.ok(camaService.listarPorEstado(estado));
    }

    @Operation(summary = "Listar todas las camas de una sala")
    @GetMapping("/sala/{sala}")
    public ResponseEntity<List<CamaResponseDTO>> listarPorSala(
            @Parameter(description = "Nombre de la sala, ej: Sala B") @PathVariable String sala) {
        return ResponseEntity.ok(camaService.listarPorSala(sala));
    }

    @Operation(summary = "Listar camas DISPONIBLES de una sala — caso de uso principal del admin")
    @GetMapping("/sala/{sala}/disponibles")
    public ResponseEntity<List<CamaResponseDTO>> listarDisponiblesPorSala(
            @Parameter(description = "Nombre de la sala, ej: Sala B") @PathVariable String sala) {
        return ResponseEntity.ok(camaService.listarDisponiblesPorSala(sala));
    }
}
