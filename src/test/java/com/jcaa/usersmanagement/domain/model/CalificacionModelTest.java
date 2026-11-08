package com.jcaa.usersmanagement.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.usersmanagement.domain.exception.InvalidCalificacionException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CalificacionModelTest {

  private static CalificacionModel build(final String estudiante, final String porcentaje,
      final String nota, final LocalDate fecha) {
    return new CalificacionModel(1L, fecha, estudiante, "Docente", "Asignatura", "Carrera",
        "Universidad", "2026-2", "Parcial",
        porcentaje == null ? null : new BigDecimal(porcentaje),
        nota == null ? null : new BigDecimal(nota));
  }

  @Test
  void createsValidModelAndTrimsText() {
    final CalificacionModel model = build("  Ana  ", "30", "4.5", LocalDate.of(2026, 1, 1));
    assertThat(model.estudiante()).isEqualTo("Ana");
    assertThat(model.withId(9L).id()).isEqualTo(9L);
  }

  @Test
  void rejectsMissingFecha() {
    assertThatThrownBy(() -> build("Ana", "30", "4.5", null))
        .isInstanceOf(InvalidCalificacionException.class).hasMessageContaining("fecha");
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  void rejectsBlankText(final String value) {
    assertThatThrownBy(() -> build(value, "30", "4.5", LocalDate.now()))
        .isInstanceOf(InvalidCalificacionException.class).hasMessageContaining("estudiante");
  }

  @Test
  void rejectsNullText() {
    assertThatThrownBy(() -> build(null, "30", "4.5", LocalDate.now()))
        .isInstanceOf(InvalidCalificacionException.class);
  }

  @Test
  void rejectsNotaOutOfRange() {
    assertThatThrownBy(() -> build("Ana", "30", "5.1", LocalDate.now()))
        .isInstanceOf(InvalidCalificacionException.class).hasMessageContaining("nota");
    assertThatThrownBy(() -> build("Ana", "30", "-1", LocalDate.now()))
        .isInstanceOf(InvalidCalificacionException.class);
  }

  @Test
  void rejectsPorcentajeOutOfRangeOrNull() {
    assertThatThrownBy(() -> build("Ana", "101", "4", LocalDate.now()))
        .isInstanceOf(InvalidCalificacionException.class).hasMessageContaining("porcentaje");
    assertThatThrownBy(() -> build("Ana", null, "4", LocalDate.now()))
        .isInstanceOf(InvalidCalificacionException.class);
  }
}
