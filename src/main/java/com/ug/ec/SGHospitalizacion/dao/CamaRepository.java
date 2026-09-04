package com.ug.ec.SGHospitalizacion.dao;

import com.ug.ec.SGHospitalizacion.domain.Cama;
import com.ug.ec.SGHospitalizacion.domain.enums.EstadoCama;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CamaRepository extends  MongoRepository<Cama, String>{

    Optional<Cama> findByNumeroCama(String numeroCama);
    List<Cama> findByEstado(EstadoCama estado);
    List<Cama> findBySala(String sala);

    //uso para el administrador
    List<Cama> findBySalaAndEstado(String Sala, EstadoCama estado);
    boolean existsByNumeroCama(String numeroCama);

}
