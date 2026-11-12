package com.jcaa.usersmanagement.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.usersmanagement.infrastructure.adapter.email.BrevoConfig;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class BrevoSpringConfigTest {

  private final BrevoSpringConfig springConfig = new BrevoSpringConfig();

  private BrevoConfig build(final String apiKey) {
    return springConfig.brevoConfig(
        "https://api.brevo.com", apiKey, "remitente@example.com", "Gestion de Usuarios",
        Duration.ofSeconds(2), Duration.ofSeconds(5), 3, Duration.ofMillis(500), 2.0,
        50, 10, 5, Duration.ofSeconds(30));
  }

  @Test
  void buildsConfigAndRestClient() {
    final BrevoConfig config = build("xkeysib-test-key");
    assertThat(config.fromAddress()).isEqualTo("remitente@example.com");
    assertThat(config.retryMaxAttempts()).isEqualTo(3);
    assertThat(springConfig.brevoRestClient(config)).isNotNull();
  }

  @Test
  void rejectsSmtpKey() {
    assertThatThrownBy(() -> build("xsmtpsib-wrong"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("xkeysib-");
  }
}
