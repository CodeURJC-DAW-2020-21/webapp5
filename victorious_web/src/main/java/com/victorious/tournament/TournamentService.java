package com.victorious.tournament;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TournamentService {

    public List<Tournament> findAll();
    public Page<Tournament> findAll(Pageable pageable);
    public Optional<Tournament> findById(Long id);
    public Optional<Tournament> findByName(String name);
    public Tournament createTournament(Tournament tournament);
    public Tournament updateTournament(Tournament tournament);
}
