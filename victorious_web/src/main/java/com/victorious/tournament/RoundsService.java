package com.victorious.tournament;

import java.util.List;
import java.util.Optional;

import com.victorious.team.Team;

public interface RoundsService {
    
    public List<Rounds> findAll();
    public Optional<Rounds> findById(Long id);
    public Rounds saveRounds(Rounds round);
    public List<Team> getWinners(Rounds round);
}
