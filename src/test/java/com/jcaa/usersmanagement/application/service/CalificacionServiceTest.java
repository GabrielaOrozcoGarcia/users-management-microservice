package com.jcaa.usersmanagement.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.CalificacionRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.CalificacionCommand;
import com.jcaa.usersmanagement.domain.exception.CalificacionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CalificacionModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {

  @Mock private CalificacionRepositoryPort repository;
  @InjectMocks private CalificacionService service;

  private final CalificacionCommand command = new CalificacionCommand(
      LocalDate.of(2026, 11, 8), "Ana", "Docente", "Electiva II", "Sistemas", "Unicolombo",
      "2026-2", "Parcial", new BigDecimal("30"), new BigDecimal("4.5"));

  @Test
  void createSavesModel() {
    final CalificacionModel saved = command.toModel(1L);
    when(repository.save(any())).thenReturn(saved);
    assertThat(service.create(command)).isEqualTo(saved);
  }

  @Test
  void updateWhenExists() {
    final CalificacionModel existing = command.toModel(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(existing));
    when(repository.update(existing)).thenReturn(existing);
    assertThat(service.update(1L, command)).isEqualTo(existing);
  }

  @Test
  void updateWhenMissingThrows() {
    when(repository.findById(5L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.update(5L, command))
        .isInstanceOf(CalificacionNotFoundException.class);
    verify(repository, never()).update(any());
  }

  @Test
  void deleteWhenExists() {
    when(repository.findById(1L)).thenReturn(Optional.of(command.toModel(1L)));
    service.delete(1L);
    verify(repository).deleteById(1L);
  }

  @Test
  void getAllDelegates() {
    when(repository.findAll()).thenReturn(List.of(command.toModel(1L)));
    assertThat(service.getAll()).hasSize(1);
  }
}
