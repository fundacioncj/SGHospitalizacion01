package com.ug.ec.SGHospitalizacion.dao;

import com.ug.ec.SGHospitalizacion.domain.ExamenMedico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenMedicoRepository extends MongoRepository<ExamenMedico, String> {
    List<ExamenMedico> findByHistoriaClinicaId(String historiaClinicaId);
    List<ExamenMedico> findByNumeroHistoriaClinica(String numeroHistoriaClinica);
    List<ExamenMedico> findByPacienteId(String pacienteId);
    List<ExamenMedico> findByMedicoId(String medicoId);
    List<ExamenMedico> findByPacienteIdAndNumeroHistoriaClinica(String pacienteId, String numeroHistoriaClinica);
}