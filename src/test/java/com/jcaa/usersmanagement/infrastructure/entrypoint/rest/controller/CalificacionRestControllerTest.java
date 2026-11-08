package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.service.CalificacionService;
import com.jcaa.usersmanagement.domain.model.CalificacionModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CalificacionRestRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalificacionRestControllerTest {

  @Mock private CalificacionService service;

  private final CalificacionRestRequest request = new CalificacionRestRequest(
      LocalDate.of(2026, 11, 8), "Ana", "Docente", "Electiva II", "Sistemas", "Unicolombo",
      "2026-2", "Parcial", new BigDecimal("30"), new BigDecimal("4.5"));

  private final CalificacionModel model = new CalificacionModel(
      1L, request.fecha(), "Ana", "Docente", "Electiva II", "Sistemas", "Unicolombo", "2026-2",
      "Parcial", new BigDecimal("30"), new BigDecimal("4.5"));

  private CalificacionRestController controller() {
    return new CalificacionRestController(service, service, service, service, service);
  }

  @Test
  void crudDelegatesToUseCases() {
    when(service.create(any())).thenReturn(model);
    when(service.update(eq(1L), any())).thenReturn(model);
    when(service.getById(1L)).thenReturn(model);
    when(service.getAll()).thenReturn(List.of(model));

    assertThat(controller().create(request).actividadEvaluada()).isEqualTo("Parcial");
    assertThat(controller().update(1L, request).id()).isEqualTo(1L);
    assertThat(controller().getById(1L).nota()).isEqualByComparingTo("4.5");
    assertThat(controller().getAll()).hasSize(1);
    controller().delete(1L);
    verify(service).delete(1L);
  }
}
