package com.jcaa.usersmanagement.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.jcaa.usersmanagement.application.port.out.EmailSenderPort;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.EmailDestinationModel;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import com.jcaa.usersmanagement.domain.valueobject.UserUsername;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailNotificationPasswordResetTest {

  @Mock private EmailSenderPort emailSenderPort;

  @Test
  void sendsTemporaryPasswordEmail() {
    final UserModel user = new UserModel(
        new UserId("u-1"), new UserName("Ana Perez"), new UserEmail("ana@example.com"),
        UserPassword.fromHash("$2a$12$hash"), UserRole.MEMBER, UserStatus.ACTIVE,
        new UserUsername("aperez"));

    new EmailNotificationService(emailSenderPort).notifyPasswordReset(user, "Temporal12345");

    final ArgumentCaptor<EmailDestinationModel> captor =
        ArgumentCaptor.forClass(EmailDestinationModel.class);
    verify(emailSenderPort).send(captor.capture());
    assertThat(captor.getValue().getDestinationEmail()).isEqualTo("ana@example.com");
    assertThat(captor.getValue().getSubject()).contains("Recuperacion");
    assertThat(captor.getValue().getBody()).contains("aperez").contains("Temporal12345")
        .doesNotContain("{{");
  }
}
