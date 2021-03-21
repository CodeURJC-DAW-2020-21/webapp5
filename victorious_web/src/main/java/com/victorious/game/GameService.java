package com.victorious.game;

import java.util.List;
import java.util.Optional;

public interface GameService {
	
	public List<Game> findAll();
	public Optional<Game> findById(Long id);
	public Optional<Game> findByName(String name);
	public Game saveGame(Game game);

}
