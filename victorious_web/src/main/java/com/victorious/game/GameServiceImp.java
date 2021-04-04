package com.victorious.game;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImp implements GameService{

	@Autowired
	private GameRepository gameRepository;
	
	@Override
	public List<Game> findAll() {
		return gameRepository.findAll();
	}

	@Override
	public Optional<Game> findById(Long id) {
		return gameRepository.findById(id);
	}

	@Override
	public Optional<Game> findByName(String name) {
		return gameRepository.findByName(name);
	}
	
	@Override
	public Game saveGame(Game game) {
		return gameRepository.save(game);
	}

	

}
