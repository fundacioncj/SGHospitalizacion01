package com.ug.ec.SGHospitalizacion.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    List<AuditLog> findByPacienteIdOrderByFechaHoraDesc(String pacienteId);

    List<AuditLog> findByCedulaPacienteOrderByFechaHoraDesc(String cedulaPaciente);

    List<AuditLog> findByModuloOrderByFechaHoraDesc(String modulo);

    List<AuditLog> findAllByOrderByFechaHoraDesc();
}