package com.victorious.tournament;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TournamentServiceImp implements TournamentService{

    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    public List<Tournament> findAll() {
        return tournamentRepository.findAll();
    }

    @Override
    public Page<Tournament> findAll(Pageable pageable) {
        return tournamentRepository.findAll(pageable);
    }

    @Override
    public Optional<Tournament> findById(Long id) {
        return tournamentRepository.findById(id);
    }

    @Override
    public Optional<Tournament> findByName(String name) {
        return tournamentRepository.findByName(name);
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament updateTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }
    
    @Override
    public Tournament saveTournament(Tournament tournament) {
    	return tournamentRepository.save(tournament);
    }   

}
