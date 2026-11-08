package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.exception.InvalidCalificacionException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad de dominio del ejercicio 21: Calificaciones.
 *
 * <p>Reglas de negocio: todos los campos de texto son obligatorios, el porcentaje de la actividad
 * evaluada va de 0 a 100 y la nota va de 0.0 a 5.0.
 */
public record CalificacionModel(
    Long id,
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

  public static final BigDecimal PORCENTAJE_MIN = BigDecimal.ZERO;
  public static final BigDecimal PORCENTAJE_MAX = new BigDecimal("100");
  public static final BigDecimal NOTA_MIN = BigDecimal.ZERO;
  public static final BigDecimal NOTA_MAX = new BigDecimal("5.0");

  public CalificacionModel {
    if (Objects.isNull(fecha)) {
      throw InvalidCalificacionException.becauseFieldIsRequired("fecha");
    }
    estudiante = requireText(estudiante, "estudiante");
    docente = requireText(docente, "docente");
    asignatura = requireText(asignatura, "asignatura");
    carrera = requireText(carrera, "carrera");
    universidad = requireText(universidad, "universidad");
    periodo = requireText(periodo, "periodo");
    actividadEvaluada = requireText(actividadEvaluada, "actividadEvaluada");
    requireRange(porcentaje, "porcentaje", PORCENTAJE_MIN, PORCENTAJE_MAX);
    requireRange(nota, "nota", NOTA_MIN, NOTA_MAX);
  }

  /** Devuelve una copia de la calificacion con el id asignado. */
  public CalificacionModel withId(final Long newId) {
    return new CalificacionModel(
        newId, fecha, estudiante, docente, asignatura, carrera, universidad, periodo,
        actividadEvaluada, porcentaje, nota);
  }

  private static String requireText(final String value, final String field) {
    if (Objects.isNull(value) || value.isBlank()) {
      throw InvalidCalificacionException.becauseFieldIsRequired(field);
    }
    return value.trim();
  }

  private static void requireRange(
      final BigDecimal value, final String field, final BigDecimal min, final BigDecimal max) {
    if (Objects.isNull(value)) {
      throw InvalidCalificacionException.becauseFieldIsRequired(field);
    }
    if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
      throw InvalidCalificacionException.becauseOutOfRange(field, min, max);
    }
  }
}
