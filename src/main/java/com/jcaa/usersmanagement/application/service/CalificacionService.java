package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateCalificacionUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteCalificacionUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllCalificacionesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetCalificacionByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateCalificacionUseCase;
import com.jcaa.usersmanagement.application.port.out.CalificacionRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.CalificacionCommand;
import com.jcaa.usersmanagement.domain.exception.CalificacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CalificacionModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Orquesta los casos de uso CRUD de Calificaciones. */
@Service
@RequiredArgsConstructor
public class CalificacionService
    implements CreateCalificacionUseCase,
        UpdateCalificacionUseCase,
        DeleteCalificacionUseCase,
        GetCalificacionByIdUseCase,
        GetAllCalificacionesUseCase {

  private final CalificacionRepositoryPort repository;

  @Override
  public CalificacionModel create(final CalificacionCommand command) {
    return repository.save(command.toModel(null));
  }

  @Override
  public CalificacionModel update(final Long id, final CalificacionCommand command) {
    ensureExists(id);
    return repository.update(command.toModel(id));
  }

  @Override
  public void delete(final Long id) {
    ensureExists(id);
    repository.deleteById(id);
  }

  @Override
  public CalificacionModel getById(final Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> CalificacionNotFoundException.becauseIdWasNotFound(id));
  }

  @Override
  public List<CalificacionModel> getAll() {
    return repository.findAll();
  }

  private void ensureExists(final Long id) {
    getById(id);
  }
}
