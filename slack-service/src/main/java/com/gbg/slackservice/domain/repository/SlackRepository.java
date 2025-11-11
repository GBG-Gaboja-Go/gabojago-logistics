package com.gbg.slackservice.domain.repository;

import com.gbg.slackservice.domain.entity.Slack;

public interface SlackRepository {

    void save(Slack slack);
}
