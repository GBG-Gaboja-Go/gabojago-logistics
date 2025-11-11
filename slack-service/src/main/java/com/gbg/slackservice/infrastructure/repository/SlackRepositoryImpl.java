package com.gbg.slackservice.infrastructure.repository;

import com.gbg.slackservice.domain.entity.Slack;
import com.gbg.slackservice.domain.repository.SlackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SlackRepositoryImpl implements SlackRepository {

    private final SlackJpaRepository slackJpaRepository;


    @Override
    public void save(Slack slack) {

        slackJpaRepository.save(slack);
    }
}
