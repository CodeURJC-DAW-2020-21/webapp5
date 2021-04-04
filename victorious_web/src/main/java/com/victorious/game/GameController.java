package com.victorious.game;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GameController {
	
	@Autowired
	GameService gameService;
	
	@GetMapping("/game/{id}/image")
	public ResponseEntity<Object> downloadImage(@PathVariable Long id) throws SQLException {

		Optional<Game> game = gameService.findById(id);
		if (game.isPresent() && game.get().getImageFile() != null) {

			Resource file = new InputStreamResource(game.get().getImageFile().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(game.get().getImageFile().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
