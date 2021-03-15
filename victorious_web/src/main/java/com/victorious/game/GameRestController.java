package com.victorious.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/games")
public class GameRestController {
	
	@Autowired
	private GameRepository gameRepository;
	
	@GetMapping(value = "/")
	@ResponseStatus(HttpStatus.OK)
	private List<Game> teams(){
		return gameRepository.findAll();
	}
}
