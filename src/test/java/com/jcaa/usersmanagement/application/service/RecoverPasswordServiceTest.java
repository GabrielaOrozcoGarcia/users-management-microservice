package com.jcaa.usersmanagement.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.UpdateUserPort;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecoverPasswordServiceTest {

  @Mock private GetUserByEmailPort getUserByEmailPort;
  @Mock private UpdateUserPort updateUserPort;
  @Mock private EmailNotificationService emailNotificationService;
  @InjectMocks private RecoverPasswordService service;

  private final UserModel user = new UserModel(
      new UserId("00000000-0000-0000-0000-000000000009"), new UserName("Ana Perez"),
      new UserEmail("ana@example.com"), UserPassword.fromHash("$2a$12$hash"),
      UserRole.MEMBER, UserStatus.ACTIVE);

  @Test
  void resetsPasswordAndSendsEmailWhenUserExists() {
    when(getUserByEmailPort.getByEmail(any())).thenReturn(Optional.of(user));
    when(updateUserPort.update(any())).thenAnswer(inv -> inv.getArgument(0));

    service.execute("ana@example.com");

    final ArgumentCaptor<String> temp = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<UserModel> saved = ArgumentCaptor.forClass(UserModel.class);
    verify(emailNotificationService).notifyPasswordReset(saved.capture(), temp.capture());
    assertThat(temp.getValue()).hasSize(12);
    assertThat(saved.getValue().getPassword().verifyPlain(temp.getValue())).isTrue();
    assertThat(saved.getValue().getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void doesNothingWhenEmailIsUnknownOrInvalid() {
    when(getUserByEmailPort.getByEmail(any())).thenReturn(Optional.empty());
    service.execute("nadie@example.com");
    service.execute("no-es-un-email");
    verify(updateUserPort, never()).update(any());
    verify(emailNotificationService, never()).notifyPasswordReset(any(), anyString());
  }

  @Test
  void generatesDifferentPasswords() {
    assertThat(RecoverPasswordService.generateTemporaryPassword())
        .isNotEqualTo(RecoverPasswordService.generateTemporaryPassword());
    verify(getUserByEmailPort, never()).getByEmail(eq(new UserEmail("x@y.co")));
  }
}
