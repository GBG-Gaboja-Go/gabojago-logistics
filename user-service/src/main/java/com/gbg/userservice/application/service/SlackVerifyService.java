package com.gbg.userservice.application.service;

import java.util.UUID;

public interface SlackVerifyService {

    void verifySlackEmail(UUID id);
}
