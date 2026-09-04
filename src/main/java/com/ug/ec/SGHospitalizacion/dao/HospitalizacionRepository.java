package com.ug.ec.SGHospitalizacion.dao;

import com.ug.ec.SGHospitalizacion.domain.Hospitalizacion;
import com.ug.ec.SGHospitalizacion.domain.enums.EstadoHospitalizacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalizacionRepository extends MongoRepository<Hospitalizacion, String> {

    Optional<Hospitalizacion> findByNumeroIngreso(String numeroIngreso);

    Optional<Hospitalizacion> findTopByOrderByNumeroIngresoDesc(); //NUEVO

    List<Hospitalizacion> findByPacienteId(String pacienteId);

    List<Hospitalizacion> findByMedicoId(String medicoId);

    List<Hospitalizacion> findByEstado(EstadoHospitalizacion estado);

    // Evita registrar al mismo paciente dos veces como ACTIVO
    boolean existsByPacienteIdAndEstado(String pacienteId, EstadoHospitalizacion estado);
}
