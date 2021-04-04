package com.victorious.tournament;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<MatchUp, Long>{
    
    Optional<MatchUp> findById(Long id);
}
