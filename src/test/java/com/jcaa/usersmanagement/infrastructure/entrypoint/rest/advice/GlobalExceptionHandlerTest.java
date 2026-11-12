package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.exception.CalificacionNotFoundException;
import com.jcaa.usersmanagement.domain.exception.InvalidCalificacionException;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void mapsDomainExceptions() {
    assertThat(handler.handleUserNotFound(UserNotFoundException.becauseIdWasNotFound("x"))
        .status()).isEqualTo(404);
    assertThat(handler.handleCalificacionNotFound(
        CalificacionNotFoundException.becauseIdWasNotFound(9L)).message()).contains("9");
    assertThat(handler.handleUserAlreadyExists(
        UserAlreadyExistsException.becauseUsernameAlreadyExists("ana")).status()).isEqualTo(409);
    assertThat(handler.handleInvalidCredentials(
        InvalidCredentialsException.becauseCredentialsAreInvalid()).status()).isEqualTo(401);
    assertThat(handler.handleDomain(InvalidCalificacionException.becauseFieldIsRequired("nota"))
        .status()).isEqualTo(400);
  }

  @Test
  void mapsInfrastructureAndUnexpectedErrors() {
    assertThat(handler.handlePersistence(
        PersistenceException.becauseConnectionFailed(new RuntimeException())).status())
        .isEqualTo(500);
    assertThat(handler.handleGeneral(new IllegalStateException()).status()).isEqualTo(500);
    final ApiErrorResponse notReadable =
        handler.handleNotReadable(mock(HttpMessageNotReadableException.class));
    assertThat(notReadable.status()).isEqualTo(400);
    assertThat(notReadable.message()).contains("yyyy-MM-dd");
  }

  @Test
  void joinsValidationMessages() {
    final ConstraintViolation<?> violation = mock(ConstraintViolation.class);
    final Path path = mock(Path.class);
    when(path.toString()).thenReturn("nota");
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn("fuera de rango");
    assertThat(handler.handleConstraintViolation(
        new ConstraintViolationException(Set.of(violation))).message())
        .isEqualTo("nota: fuera de rango");

    final MethodArgumentNotValidException invalid = mock(MethodArgumentNotValidException.class);
    final BindingResult binding = mock(BindingResult.class);
    when(invalid.getBindingResult()).thenReturn(binding);
    when(binding.getFieldErrors())
        .thenReturn(List.of(new FieldError("req", "fecha", "no puede ser nula")));
    assertThat(handler.handleMethodArgumentNotValid(invalid).message())
        .isEqualTo("fecha: no puede ser nula");
  }
}
