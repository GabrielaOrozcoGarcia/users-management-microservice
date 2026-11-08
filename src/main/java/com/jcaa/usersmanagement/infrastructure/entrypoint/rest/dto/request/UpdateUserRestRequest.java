package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRestRequest(
    @NotBlank(message = "name must not be blank")
        @Size(min = 3, message = "name must have at least 3 characters")
        String name,
    @NotBlank(message = "email must not be blank")
        @Email(message = "email must be a valid email address")
        String email,
    String password,
    @NotBlank(message = "role must not be blank") String role,
    @NotBlank(message = "status must not be blank") String status,
    @Schema(example = "gorozco", description = "Opcional: si no se envia se conserva el actual")
        @Size(max = 50)
        String username) {

  public UpdateUserRestRequest(
      final String name, final String email, final String password, final String role,
      final String status) {
    this(name, email, password, role, status, null);
  }
}
