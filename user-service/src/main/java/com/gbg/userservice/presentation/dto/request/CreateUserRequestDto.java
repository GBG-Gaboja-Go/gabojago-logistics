package com.gbg.userservice.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateUserRequestDto(

    @NotNull(message = "사용자 이름은 반드시 입력해야 합니다.")
    String username,

    @Size(max = 20)
    String nickname,

    @NotNull(message = "사용자 비밀번호는 반드시 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,15}$",
        message = "비밀번호는 8~15자이며, 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    String password,

    @NotNull(message = "소속된 업체명을 알고있는지 반드시 입력해야 합니다. TRUE | FALSE")
    boolean hasCompanyId,

    UUID organization,

    @NotNull(message = "사용자 슬랙 이메일주소는 반드시 입력해야 합니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    ,message = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    String slackEmail,

    @Size(max = 50)
    String summary
    ) {

}