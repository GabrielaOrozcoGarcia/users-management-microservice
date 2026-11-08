package com.jcaa.usersmanagement.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.usersmanagement.domain.exception.InvalidUserUsernameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserUsernameTest {

  @Test
  void normalizesToLowerCase() {
    assertThat(new UserUsername("  GOrozco ").value()).isEqualTo("gorozco");
    assertThat(new UserUsername("ana.p_1").toString()).isEqualTo("ana.p_1");
  }

  @ParameterizedTest
  @ValueSource(strings = {"ab", "con espacio", "ñandu", "a@b"})
  void rejectsInvalidFormat(final String value) {
    assertThatThrownBy(() -> new UserUsername(value))
        .isInstanceOf(InvalidUserUsernameException.class);
  }

  @Test
  void rejectsEmpty() {
    assertThatThrownBy(() -> new UserUsername("  "))
        .isInstanceOf(InvalidUserUsernameException.class).hasMessageContaining("vacio");
  }

  @Test
  void derivesFromEmail() {
    assertThat(UserUsername.fromEmail(new UserEmail("Admin@example.com")).value())
        .isEqualTo("admin");
    assertThat(UserUsername.fromEmail(new UserEmail("a+b@x.co")).value()).isEqualTo("ab_");
    assertThat(UserUsername.ofNullable(null, new UserEmail("ana@x.co")).value()).isEqualTo("ana");
    assertThat(UserUsername.ofNullable("pepe", new UserEmail("ana@x.co")).value())
        .isEqualTo("pepe");
  }
}
