package com.gbg.hubservice.application.service.exception;

import lombok.Getter;

@Getter
public class HubException extends RuntimeException {

    private final HubErrorCode errorCode;

    public HubException(HubErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
