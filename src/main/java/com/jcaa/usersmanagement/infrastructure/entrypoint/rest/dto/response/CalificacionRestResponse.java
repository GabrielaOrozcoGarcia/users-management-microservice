package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CalificacionRestResponse(
    Long id,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
    String estudiante,
    String docente,
    String asignatura,
    String carrera,
    String universidad,
    String periodo,
    String actividadEvaluada,
    BigDecimal porcentaje,
    BigDecimal nota) {}
