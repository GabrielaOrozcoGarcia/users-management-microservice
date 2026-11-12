package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Recorre todo el menu de consola con entradas simuladas y un controlador simulado. */
class UserManagementCliTest {

  private static final UserResponse USER =
      new UserResponse("u-1", "Ana Perez", "ana@example.com", "MEMBER", "ACTIVE");

  private String run(final UserController controller, final String... lines) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ConsoleIO console = new ConsoleIO(
        new Scanner(String.join("\n", lines) + "\n"),
        new PrintStream(out, true, StandardCharsets.UTF_8));
    new UserManagementCli(controller, console).start();
    return out.toString(StandardCharsets.UTF_8);
  }

  @Test
  void happyPathThroughEveryOption() {
    final UserController controller = mock(UserController.class);
    when(controller.listAllUsers()).thenReturn(List.of(USER));
    when(controller.findUserById("u-1")).thenReturn(USER);
    when(controller.createUser(any())).thenReturn(USER);
    when(controller.updateUser(any())).thenReturn(USER);
    when(controller.login(any())).thenReturn(USER);

    final String output = run(controller,
        "abc", "9",                                              // entrada invalida y opcion inexistente
        "1",                                                     // listar
        "2", "", "u-1",                                          // buscar (primero vacio)
        "3", "u-1", "Ana Perez", "ana@example.com", "Clave12345", "MEMBER",
        "4", "u-1", "Ana Perez", "ana@example.com", "", "MEMBER", "ACTIVE",
        "4", "u-1", "Ana Perez", "ana@example.com", "Nueva12345", "MEMBER", "ACTIVE",
        "5", "u-1",
        "6", "ana@example.com", "Clave12345",
        "0");

    assertThat(output)
        .contains("Invalid input. Please enter a number.")
        .contains("Invalid option")
        .contains("Total: 1 user(s)")
        .contains("Value cannot be blank")
        .contains("User created successfully.")
        .contains("User updated successfully.")
        .contains("User deleted successfully.")
        .contains("Login successful.")
        .contains("Goodbye!");
  }

  @Test
  void showsErrorsReturnedByTheController() {
    final UserController controller = mock(UserController.class);
    when(controller.listAllUsers()).thenReturn(List.of());
    when(controller.findUserById(anyString()))
        .thenThrow(UserNotFoundException.becauseIdWasNotFound("x"));
    when(controller.createUser(any()))
        .thenThrow(UserAlreadyExistsException.becauseEmailAlreadyExists("a@x.co"));
    when(controller.updateUser(any()))
        .thenThrow(UserNotFoundException.becauseIdWasNotFound("x"));
    doThrow(UserNotFoundException.becauseIdWasNotFound("x")).when(controller).deleteUser("x");
    when(controller.login(any()))
        .thenThrow(InvalidCredentialsException.becauseCredentialsAreInvalid())
        .thenThrow(new ConstraintViolationException(Set.of(violation())))
        .thenThrow(new IllegalStateException("falla"));

    final String output = run(controller,
        "1",
        "2", "x",
        "3", "x", "Ana", "a@x.co", "Clave12345", "MEMBER",
        "4", "x", "Ana", "a@x.co", "", "MEMBER", "ACTIVE",
        "5", "x",
        "6", "a@x.co", "Clave12345",
        "6", "a@x.co", "Clave12345",
        "6", "a@x.co", "Clave12345",
        "0");

    assertThat(output)
        .contains("No users found.")
        .contains("Not found:")
        .contains("Error: A user with email")
        .contains("Error: Usuario/correo o contraseña incorrectos.")
        .contains("Validation errors:")
        .contains("    - campo invalido")
        .contains("Unexpected error: falla");
  }

  private static ConstraintViolation<?> violation() {
    final ConstraintViolation<?> violation = mock(ConstraintViolation.class);
    when(violation.getMessage()).thenReturn("campo invalido");
    return violation;
  }
}
