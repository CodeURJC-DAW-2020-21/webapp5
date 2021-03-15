package com.victorious.game;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long>{
	
	Optional<Game> findById(Long id);
	Optional<Game> findByName(String name);
}
