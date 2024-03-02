package com.recommtoon.recommtoonapi.account.dto;

import com.recommtoon.recommtoonapi.account.entity.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterDto {

    @NotNull
    private String realName;

    @NotNull
    private String username;

    @NotNull
    private String nickname;

    @NotNull
    private String password;

    private Gender gender;
    private String mbtiType;
}
