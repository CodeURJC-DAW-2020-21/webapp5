package com.victorious.tournament;

import java.util.List;
import java.util.Optional;

public interface RoundsService {
    
    public List<Rounds> findAll();
    public Optional<Rounds> findById(Long id);
}
