package com.victorious.team;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamRestController {
	
	@Autowired
	private TeamService teamService;
	
	//Create a new team
	@PostMapping(value="/")
	public ResponseEntity<?> createTeam (@RequestBody Team team){
		return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(team));
	}
	
	//Read a Team
	@GetMapping("/{id}")
	public ResponseEntity<?> team (@PathVariable Long id){
		Optional<Team> oTeam = teamService.findById(id);
		if(!oTeam.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(oTeam);
	}
	
	//Update a Team
	@PutMapping("/{id}")
	public ResponseEntity<?> updateTeam (@RequestBody Team team, @PathVariable Long id){
		Optional<Team> oTeam = teamService.findById(id);
		if(!oTeam.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		//Updates para el Team
		//Ejemplo: team.get().setName(teamDetails.getName());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(teamService.updateTeam(team));
	}
	
    /*//Delete a Team
	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteTeam (@PathVariable Long id){
		if(!teamService.findById(id).isPresent()) {
			return ResponseEntity.notFound().build();
		}
		teamService.deleteById(id);
		return ResponseEntity.ok().build();
	}*/
}
