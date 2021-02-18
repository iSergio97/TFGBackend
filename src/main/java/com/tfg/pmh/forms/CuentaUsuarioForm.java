package com.tfg.pmh.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaUsuarioForm {

    @NotNull
    private Long id;

    @NotBlank
    private String newUsername;

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
