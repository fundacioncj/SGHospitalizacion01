package com.ug.ec.SGHospitalizacion.dao;

import com.ug.ec.SGHospitalizacion.domain.HistoriaClinica;
import com.ug.ec.SGHospitalizacion.dto.response.HistoriaClinicaResponseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends MongoRepository<HistoriaClinica, String> {
    Optional<HistoriaClinica> findByNumeroHistoriaClinicaAndPacienteId(String numeroHistoriaClinica, String pacienteId);
    Optional<HistoriaClinica> findByPacienteId(String pacienteId);
    Optional<HistoriaClinica> findByNumeroHistoriaClinica(String numeroHistoriaClinica);
    boolean existsByPacienteId(String pacienteId);
}
