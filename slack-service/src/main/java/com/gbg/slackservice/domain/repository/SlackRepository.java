package com.gbg.slackservice.domain.repository;

import com.gbg.slackservice.domain.entity.Slack;
import java.util.List;

public interface SlackRepository {

    void save(Slack slack);

    List<Slack> findAll();
}
