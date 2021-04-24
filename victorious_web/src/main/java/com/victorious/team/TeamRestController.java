package com.victorious.team;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

import com.fasterxml.jackson.annotation.JsonView;
import com.victorious.ImageService;
import com.victorious.game.Game;
import com.victorious.game.GameService;
import com.victorious.user.User;
import com.victorious.user.UserService;

@RestController
@RequestMapping("/api/teams")
public class TeamRestController {
	
	interface TeamDetails extends User.Basic, Team.Basic, Game.Basic, Team.TeamTournament, Team.TeamUsers{}
	
	private static final String POSTS_FOLDER = "posts";
	
	@Autowired
	private TeamService teamService;
	
	@Autowired 
	private ImageService imgService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GameService gameService;
	
	@PostMapping(value="/")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> createTeam (@RequestBody Team team){
		
		Team newTeam = new Team(team.getName(), team.getDescription());
		URI location = fromCurrentRequest().path("/{id}")
				.buildAndExpand(newTeam.getId()).toUri();
		
		teamService.saveTeam(newTeam);
		return ResponseEntity.created(location).body(newTeam);
	}
	
	@GetMapping("/")
	@JsonView(TeamDetails.class)
	public ResponseEntity<List<Team>> getTeams(){
		List<Team> teams = teamService.findAll();
		if(teams == null) {
			return ResponseEntity.notFound().build();
		}else{
			return ResponseEntity.ok(teams);
		}
	}
	
	@GetMapping("/{id}")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> team (@PathVariable Long id){
		
		Optional<Team> tTeam = teamService.findById(id);
		if(!tTeam.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(tTeam.get());
	}
	
	@PutMapping("/{id}")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> updateTeam (@RequestBody Team team, @PathVariable Long id){
		
		Optional<Team> oTeam = teamService.findById(id);
		if(!oTeam.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		oTeam.get().setName(team.getName());
		oTeam.get().setDescription(team.getDescription());
		teamService.updateTeam(oTeam.get());
		return ResponseEntity.status(HttpStatus.OK).body(oTeam.get());
	}
	
	@GetMapping("/league")
	@JsonView(TeamDetails.class)
	public ResponseEntity<List<Team>> getLeague(){
		
		List<Team> league = teamService.findAll(Sort.by(Sort.Direction.DESC, "nVictories"));
		if(league == null) {
			return ResponseEntity.notFound().build();
		}else{
			return ResponseEntity.status(HttpStatus.OK).body(league);
		}
	}
	
	@PostMapping("{id}/requests/")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> requestToJoin(@RequestBody User user, @PathVariable Long id){
		
		Team team = teamService.findById(id).get();
		Long userId = userService.findByName(user.getName()).get().getId();
		if (!team.getRequests().contains(userId)) {
			team.getRequests().add(userId);
			teamService.saveTeam(team);
			return ResponseEntity.status(HttpStatus.CREATED).body(team);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("{id}/members/")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> LeaveFromTeam(HttpServletRequest request, @PathVariable Long id){
		
		String loggedUserName = request.getUserPrincipal().getName();
		User loggedUser = userService.findByName(loggedUserName).get();
		Team team = teamService.findById(id).get();
		if (loggedUser.getTeam() != null && loggedUser.getTeam().equals(team)) {
			if (team.isAdmin(loggedUser)) {
				team.getAdmins().remove(loggedUser);
			}
			loggedUser.setTeam(null);
			userService.saveUser(loggedUser);
			return ResponseEntity.status(HttpStatus.OK).body(team);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("{id}/members/{userId}")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> kickMemberFromTeam(HttpServletRequest request, @PathVariable Long userId, @PathVariable Long id){
		
		String loggedUserName = request.getUserPrincipal().getName();
		User loggedUser = userService.findByName(loggedUserName).get();
		Team team = teamService.findById(id).get();
		User user = userService.findById(userId).get();
		boolean isAdmin = team.isAdmin(loggedUser) || loggedUser.getRoles().contains("ADMIN") || user.equals(loggedUser);
		if (user.getTeam() != null && user.getTeam().equals(team) && isAdmin) {
			if (team.isAdmin(user)) {
				team.getAdmins().remove(user);
			}
			user.setTeam(null);
			userService.saveUser(user);
			return ResponseEntity.status(HttpStatus.OK).body(team);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("{id}/admins/")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> addUserToAdmins(HttpServletRequest request, @PathVariable Long id, @RequestBody User user){		
		
		String loggedUserName = request.getUserPrincipal().getName();
		User loggedUser = userService.findByName(loggedUserName).get();
		User oUser = userService.findByName(user.getName()).get();
		Team team = teamService.findById(id).get();
		boolean isAdmin = team.isAdmin(loggedUser) || loggedUser.getRoles().contains("ADMIN");
		if (!team.isAdmin(oUser) && oUser.getTeam().equals(team) && isAdmin) {
			team.addAdmin(oUser);
			teamService.saveTeam(team);
			return ResponseEntity.status(HttpStatus.OK).body(team);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("{id}/requests/{accept}")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> addUserToTeam(HttpServletRequest request, @PathVariable Long id, @RequestBody User user,
			@PathVariable boolean accept) {

		String loggedUserName = request.getUserPrincipal().getName();
		User loggedUser = userService.findByName(loggedUserName).get();
		Team team = teamService.findById(id).get();
		User oUser = userService.findByName(user.getName()).get();
		Long userId = oUser.getId();
		boolean isAdmin = team.isAdmin(loggedUser) || loggedUser.getRoles().contains("ADMIN");

		if (isAdmin) {
			if (accept) {
				if (team.getRequests().contains(userId) && user.getTeam() == null) {
					team.getRequests().remove(userId);
					oUser.setTeam(team);
					userService.saveUser(oUser);
					teamService.saveTeam(team);
					return ResponseEntity.status(HttpStatus.CREATED).body(team);
				} else {
					team.getRequests().remove(userId);
					return ResponseEntity.badRequest().build();
				}
			} else {
				if (team.getRequests().contains(userId)) {
					team.getRequests().remove(userId);
					teamService.saveTeam(team);
					return ResponseEntity.status(HttpStatus.OK).body(team);
				} else {
					team.getRequests().remove(userId);
					return ResponseEntity.badRequest().build();
				}
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("{id}/games/")
	@JsonView(TeamDetails.class)
	public ResponseEntity<Team> addGameToTeam(HttpServletRequest request, @PathVariable Long id, @RequestBody Game game) {
		
		String loggedUserName = request.getUserPrincipal().getName();
		User loggedUser = userService.findByName(loggedUserName).get();
		Team team = teamService.findById(id).get();
		boolean isAdmin = team.isAdmin(loggedUser) || loggedUser.getRoles().contains("ADMIN");
		if (isAdmin) {
			Game oGame = gameService.findByName(game.getName()).get();
			team.getGames().add(oGame);
			teamService.saveTeam(team);
			return ResponseEntity.status(HttpStatus.CREATED).body(team);
		} else {
			return ResponseEntity.badRequest().build();
		}
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
	
	@GetMapping("/{id}/image")
	public ResponseEntity<Object> downloadUserImage(@PathVariable Long id, HttpServletRequest request) throws MalformedURLException {
		
		Optional<Team> team = teamService.findById(id);
		
		if (team.isPresent()) {
			return this.imgService.createResponseFromImage(POSTS_FOLDER, team.get().getId());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
    
    @PostMapping("/{id}/image")
	public ResponseEntity<Object> uploadUserImage(@PathVariable Long id, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException {

    	Optional<Team> team = teamService.findById(id);
    	
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
