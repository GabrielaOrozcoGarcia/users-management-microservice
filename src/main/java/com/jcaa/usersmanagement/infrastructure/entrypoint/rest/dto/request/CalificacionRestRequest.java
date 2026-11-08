package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CalificacionRestRequest(
    @Schema(example = "2026-11-08") @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
    @Schema(example = "Ana Perez") @NotBlank @Size(max = 150) String estudiante,
    @Schema(example = "John Arrieta") @NotBlank @Size(max = 150) String docente,
    @Schema(example = "Electiva II") @NotBlank @Size(max = 150) String asignatura,
    @Schema(example = "Ingenieria de Sistemas") @NotBlank @Size(max = 150) String carrera,
    @Schema(example = "Unicolombo") @NotBlank @Size(max = 150) String universidad,
    @Schema(example = "2026-2") @NotBlank @Size(max = 20) String periodo,
    @Schema(example = "Parcial 1") @NotBlank @Size(max = 150) String actividadEvaluada,
    @Schema(example = "30") @NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal porcentaje,
    @Schema(example = "4.5") @NotNull @DecimalMin("0.0") @DecimalMax("5.0") BigDecimal nota) {}
