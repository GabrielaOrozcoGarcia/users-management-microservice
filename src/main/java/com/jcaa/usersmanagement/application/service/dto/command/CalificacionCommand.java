package com.jcaa.usersmanagement.application.service.dto.command;

import com.jcaa.usersmanagement.domain.model.CalificacionModel;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CalificacionCommand(
    LocalDate fecha,
    String estudiante,
    String docente,
    String asignatura,
    String carrera,
    String universidad,
    String periodo,
    String actividadEvaluada,
    BigDecimal porcentaje,
    BigDecimal nota) {

  public CalificacionModel toModel(final Long id) {
    return new CalificacionModel(
        id, fecha, estudiante, docente, asignatura, carrera, universidad, periodo,
        actividadEvaluada, porcentaje, nota);
  }
}
