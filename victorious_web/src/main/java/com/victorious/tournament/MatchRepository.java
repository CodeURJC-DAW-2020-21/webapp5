package com.victorious.tournament;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>{
    
    Optional<Match> findById(Long id);
}
