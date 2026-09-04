package com.ug.ec.SGHospitalizacion.controller;

import com.ug.ec.SGHospitalizacion.dto.external.PacienteExternoDTO;
import com.ug.ec.SGHospitalizacion.infrastructure.client.SgbCitaMedicaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final SgbCitaMedicaClient sgbClient;

    @GetMapping("/cedula/{cedula}")
    public PacienteExternoDTO buscarPaciente(
            @PathVariable String cedula) {

        return sgbClient.obtenerPacientePorCedula(cedula);
    }
}
