package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.application.port.in.CreateCalificacionUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteCalificacionUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllCalificacionesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetCalificacionByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateCalificacionUseCase;
import com.jcaa.usersmanagement.infrastructure.config.OpenApiConfig;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CalificacionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.CalificacionRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper.CalificacionRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
@Tag(name = "Calificaciones", description = "CRUD de calificaciones (Ejercicio 21)")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class CalificacionRestController {

  private final CreateCalificacionUseCase createUseCase;
  private final UpdateCalificacionUseCase updateUseCase;
  private final DeleteCalificacionUseCase deleteUseCase;
  private final GetCalificacionByIdUseCase getByIdUseCase;
  private final GetAllCalificacionesUseCase getAllUseCase;

  @Operation(summary = "Listar todas las calificaciones")
  @GetMapping
  public List<CalificacionRestResponse> getAll() {
    return CalificacionRestMapper.toResponseList(getAllUseCase.getAll());
  }

  @Operation(summary = "Obtener una calificacion por id")
  @GetMapping("/{id}")
  public CalificacionRestResponse getById(@PathVariable final Long id) {
    return CalificacionRestMapper.toResponse(getByIdUseCase.getById(id));
  }

  @Operation(summary = "Registrar una calificacion")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CalificacionRestResponse create(
      @Valid @RequestBody final CalificacionRestRequest request) {
    return CalificacionRestMapper.toResponse(
        createUseCase.create(CalificacionRestMapper.toCommand(request)));
  }

  @Operation(summary = "Actualizar una calificacion")
  @PutMapping("/{id}")
  public CalificacionRestResponse update(
      @PathVariable final Long id, @Valid @RequestBody final CalificacionRestRequest request) {
    return CalificacionRestMapper.toResponse(
        updateUseCase.update(id, CalificacionRestMapper.toCommand(request)));
  }

  @Operation(summary = "Eliminar una calificacion")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final Long id) {
    deleteUseCase.delete(id);
  }
}
