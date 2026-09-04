package com.ug.ec.SGHospitalizacion.dao;

import com.ug.ec.SGHospitalizacion.domain.Anamnesis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnamnesisRepository extends MongoRepository<Anamnesis, String> {
    List<Anamnesis> findByHistoriaClinicaId(String hcId);
    List<Anamnesis> findByPacienteId(String pacienteId);
    List<Anamnesis> findByMedicoId(String medicoId);
    //boolean existsByHistoriaClinicaId(String historiaClinicaId); //ACTUALIZANDO
}
