package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Registro de usuario. {@code id} es opcional (si no se envia se genera un UUID), {@code role} es
 * opcional (por defecto MEMBER) y {@code username} es opcional (por defecto la parte del email
 * antes de la arroba).
 */
public record CreateUserRestRequest(
    @Schema(description = "Opcional: si no se envia se genera un UUID") String id,
    @NotBlank(message = "name must not be blank")
        @Size(min = 3, message = "name must have at least 3 characters")
        String name,
    @NotBlank(message = "email must not be blank")
        @Email(message = "email must be a valid email address")
        String email,
    @NotBlank(message = "password must not be blank")
        @Size(min = 8, message = "password must have at least 8 characters")
        String password,
    @Schema(description = "Opcional: ADMIN, MEMBER o REVIEWER (por defecto MEMBER)") String role,
    @Schema(example = "gorozco") @Size(max = 50) String username) {

  public CreateUserRestRequest(
      final String id, final String name, final String email, final String password,
      final String role) {
    this(id, name, email, password, role, null);
  }
}
