package com.jcaa.usersmanagement.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DataSourceSpringConfigTest {

  @Test
  void triesToOpenThePoolWithTheConfiguredProperties() {
    final DataSourceSpringConfig config = new DataSourceSpringConfig();
    ReflectionTestUtils.setField(config, "dbHost", "127.0.0.1");
    ReflectionTestUtils.setField(config, "dbPort", 1);
    ReflectionTestUtils.setField(config, "dbName", "crud_usuarios");
    ReflectionTestUtils.setField(config, "dbUsername", "root");
    ReflectionTestUtils.setField(config, "dbPassword", "secret");
    ReflectionTestUtils.setField(config, "dbSslMode", "DISABLED");

    // No hay base de datos en el puerto 1: Hikari falla al inicializar el pool.
    assertThatThrownBy(config::dataSource).isInstanceOf(RuntimeException.class);
  }

  @Test
  void configurationExceptionKeepsMessage() {
    final RuntimeException cause = new RuntimeException("io");
    final ConfigurationException exception = ConfigurationException.becauseLoadFailed(cause);
    org.assertj.core.api.Assertions.assertThat(exception)
        .hasMessageContaining("configuration")
        .hasCause(cause);
  }
}
