package com.acme.learningcenter.security.domain.service.communication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AuthenticateRequest {

  @NotNull
  private String email;
  @NotNull
  @NotBlank
  private String password;
}
