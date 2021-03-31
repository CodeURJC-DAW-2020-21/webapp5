package com.victorious.tournament;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImp implements MatchService{

    @Autowired
    private MatchRepository matchRepository;

    @Override
    public MatchUp createMatch(MatchUp match) {
        return matchRepository.save(match);
    }

    @Override
    public MatchUp saveMatch(MatchUp match) {
        return matchRepository.save(match);
    }

    @Override
    public Optional<MatchUp> findById(Long id) {
        return matchRepository.findById(id);
    }
    
}
