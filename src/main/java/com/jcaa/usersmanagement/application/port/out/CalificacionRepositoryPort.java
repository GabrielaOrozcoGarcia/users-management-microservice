package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.CalificacionModel;
import java.util.List;
import java.util.Optional;

/** Puerto de salida para persistir calificaciones. */
public interface CalificacionRepositoryPort {

  CalificacionModel save(CalificacionModel calificacion);

  CalificacionModel update(CalificacionModel calificacion);

  Optional<CalificacionModel> findById(Long id);

  List<CalificacionModel> findAll();

  void deleteById(Long id);
}
