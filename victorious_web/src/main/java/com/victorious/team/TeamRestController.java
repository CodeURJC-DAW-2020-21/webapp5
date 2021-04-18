package com.victorious.team;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.victorious.ImageService;

@RestController
@RequestMapping("/api/teams")
public class TeamRestController {
	
	private static final String POSTS_FOLDER = "posts";
	
	@Autowired
	private TeamService teamService;
	
	@Autowired 
	private ImageService imgService;
	
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
	
	@GetMapping("/{name}/image")
	public ResponseEntity<Object> downloadUserImage(@PathVariable String name, HttpServletRequest request) throws MalformedURLException {
		
		Optional<Team> team = teamService.findByName(name);
		
		if (team.isPresent()) {
			return this.imgService.createResponseFromImage(POSTS_FOLDER, team.get().getId());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
    
    @PostMapping("/{name}/image")
	public ResponseEntity<Object> uploadUserImage(@PathVariable String name, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException {

    	Optional<Team> team = teamService.findByName(name);
    	
		if (team.isPresent()) {
			
			URI location = fromCurrentRequest().build().toUri();
			
			team.get().setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
			teamService.saveTeam(team.get());
			imgService.saveImage(POSTS_FOLDER, team.get().getId(), imageFile);
			
			return ResponseEntity.created(location).build();

		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
