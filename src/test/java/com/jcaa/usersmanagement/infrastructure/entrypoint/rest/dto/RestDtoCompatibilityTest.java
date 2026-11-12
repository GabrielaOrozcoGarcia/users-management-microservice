package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.UserPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateUserRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.LoginRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.RecoverPasswordRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateUserRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.MessageRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.UserRestResponse;
import org.junit.jupiter.api.Test;

class RestDtoCompatibilityTest {

  @Test
  void legacyConstructorsLeaveUsernameEmpty() {
    assertThat(new CreateUserRestRequest("1", "Ana", "a@x.co", "Clave12345", "MEMBER").username())
        .isNull();
    assertThat(new UpdateUserRestRequest("Ana", "a@x.co", "", "MEMBER", "ACTIVE").username())
        .isNull();
    assertThat(new UserRestResponse("1", "Ana", "a@x.co", "MEMBER", "ACTIVE").username()).isNull();
    assertThat(new UserPersistenceDto("1", "Ana", "a@x.co", "h", "MEMBER", "ACTIVE", null, null)
        .username()).isNull();
  }

  @Test
  void loginUsesEmailOrUsername() {
    assertThat(new LoginRestRequest("a@x.co", "Clave12345").identifier()).isEqualTo("a@x.co");
    final LoginRestRequest byUsername = new LoginRestRequest(null, "admin", "Clave12345");
    assertThat(byUsername.identifier()).isEqualTo("admin");
    assertThat(byUsername.isIdentifierPresent()).isTrue();
    assertThat(new LoginRestRequest("", " ", "Clave12345").isIdentifierPresent()).isFalse();
    assertThat(new LoginRestRequest(null, null, "Clave12345").isIdentifierPresent()).isFalse();
  }

  @Test
  void simpleRecords() {
    assertThat(new RecoverPasswordRestRequest("a@x.co").email()).isEqualTo("a@x.co");
    assertThat(new MessageRestResponse("ok").message()).isEqualTo("ok");
  }
}
