package com.victorious.tournament;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.victorious.game.GameService;
import com.victorious.team.Team;
import com.victorious.team.TeamService;
import com.victorious.user.UserService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/tournaments")
public class TournamentRestController {
		
		interface TournamentDetails extends Tournament.Basic, Tournament.Teams, Team.Basico {}
	
		@Autowired
	    TournamentService tournamentService;

		@Autowired
		GameService gameService;
		
		@Autowired
		TeamService teamService;
		
		@Autowired
		UserService userService;
		/*
		@ModelAttribute
		public void addAttributes(Model model, HttpServletRequest request) {

			Principal principal = request.getUserPrincipal();

			if (principal != null) {

				model.addAttribute("logged", true);
				model.addAttribute("userName", principal.getName());
				model.addAttribute("admin", request.isUserInRole("ADMIN"));

			} else {
				model.addAttribute("logged", false);
			}
		}
		
	*/
	// get tournaments
		@GetMapping("/pages")
		public ResponseEntity <Page <Tournament>> getTournametPage (Pageable page){
			Page <Tournament> tournamentPage = tournamentService.findAll(page);
			
			return ResponseEntity.ok(tournamentPage);
		}

		@JsonView(TournamentDetails.class)
		@GetMapping("/")
		public ResponseEntity <Collection<Tournament>> getTournaments(){
			Collection<Tournament> tournaments = tournamentService.findAll();
			if(tournaments == null) {
				return ResponseEntity.notFound().build();
			}else{
				return ResponseEntity.ok(tournaments);
			}
			
		}
	
		@GetMapping("/{id}")
		@JsonView(TournamentDetails.class)
		public  ResponseEntity <Tournament> getTournament( @PathVariable Long id) {
			
			Optional<Tournament> tournament = tournamentService.findById(id);

			if(tournament.isPresent()) {
				return ResponseEntity.ok(tournament.get());
			}else{
				return ResponseEntity.notFound().build();				
			}

		}
		
		// NO FUNCIONA FORBIDEN
		
		@PostMapping("/new-tournament")
		public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament){
			tournamentService.saveTournament(tournament);
			
			URI location = fromCurrentRequest().path("/{id}")
					.buildAndExpand(tournament.getId()).toUri();
			
			return ResponseEntity.created(location).body(tournament);
			
		}
		
		// addTeam
		
		@PutMapping("/{id}")
		public ResponseEntity<Tournament> replaceTournament(@PathVariable long id,
			@RequestBody Tournament newTournament){
			Optional <Tournament> tournament = tournamentService.findById(id);
			
			if (tournament.isPresent()) {
				newTournament.setId(id);
				tournamentService.saveTournament(newTournament);
				
				return ResponseEntity.ok(tournament.get());
				
			}else{
				
				return ResponseEntity.notFound().build();
			}
			
		}
}
