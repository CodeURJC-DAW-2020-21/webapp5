package com.victorious.tournament;

import java.util.Optional;

public interface MatchService {
    
    public MatchUp createMatch(MatchUp match);
    public MatchUp saveMatch(MatchUp match);
    public Optional<MatchUp> findById(Long id);
}
