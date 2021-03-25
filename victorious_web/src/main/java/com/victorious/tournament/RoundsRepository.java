package com.victorious.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundsRepository extends JpaRepository<Rounds, Long>{
    
    
}
