package com.victorious.tournament;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
		
		interface TournamentDetails extends Tournament.Basic, Tournament.Teams, Team.Basic {}
		interface RoundDetails extends Rounds.Basic, Team.Basic, MatchUp.Basic {}
		interface MatchDetails extends MatchUp.Basic, Team.Basic{}
	
		@Autowired
	    TournamentService tournamentService;

		@Autowired
		GameService gameService;
		
		@Autowired
		TeamService teamService;
		
		@Autowired
		UserService userService;
		
		@Autowired
		RoundsService roundsService; 
		
		@Autowired
		MatchService matchService; 
		
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
	
		@PostMapping("/new-tournament")
		public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament){
			
			if (!tournamentService.findById(tournament.getId()).isPresent() && 
			!tournamentService.findByName(tournament.getName()).isPresent()) { // IF the new tournament not exists
				
				tournamentService.saveTournament(tournament);
				
				URI location = fromCurrentRequest().path("/{id}")
						.buildAndExpand(tournament.getId()).toUri();
				
				return ResponseEntity.created(location).body(tournament);

			}else{
			
				return ResponseEntity.notFound().build();
			}
		}
			
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
		
		@JsonView(Team.Basic.class)
		@GetMapping("/{tournamentId}/participants")
		public ResponseEntity< Collection<Team> > getTournamentParticipants(@PathVariable Long tournamentId){
			Optional<Tournament> tournament = tournamentService.findById(tournamentId);

			if(tournament.isPresent()) {
				return ResponseEntity.ok(tournament.get().getParticipants());
			}else{
				return ResponseEntity.notFound().build();				
			}
		}

		@JsonView(Team.Basic.class)
		@GetMapping("/{tournamentId}/participants/{teamId}")
		public ResponseEntity<Team> getTournamentParticipant(@PathVariable Long tournamentId, @PathVariable Long teamId ){
			Optional<Tournament> tournament = tournamentService.findById(tournamentId);
			Optional<Team> team = teamService.findById(teamId);
			
			if(tournament.isPresent() && team.isPresent()) {
				if(tournament.get().getParticipants().contains(team.get())) {
					return ResponseEntity.ok(team.get());
				}else{
					return ResponseEntity.notFound().build();				

				}
			}else{
				return ResponseEntity.notFound().build();				
			}
		}
				
	    @PostMapping("/{tournamentId}/participants")
		public ResponseEntity<Team> addParticipantToTournament(@RequestBody Team team, @PathVariable Long tournamentId){
			Optional<Tournament> tournament = tournamentService.findById(tournamentId);
			
			teamService.saveTeam(team);
			if(!tournament.get().getParticipants().contains(team)) {
				tournament.get().addTeam(team);
				tournamentService.saveTournament(tournament.get());
				
				URI location = fromCurrentRequest().path("/{id}")
						.buildAndExpand(team.getId()).toUri();
					
				return ResponseEntity.created(location).body(team);
				
			}else {
				return ResponseEntity.notFound().build();				
			}		
		}
	    
	    @GetMapping("/{tournamentId}/rounds")
	    @JsonView(RoundDetails.class)
		public ResponseEntity< Collection<Rounds> > getTournamentRounds(@PathVariable Long tournamentId ){
			Optional<Tournament> tournament = tournamentService.findById(tournamentId);
			
			if(tournament.isPresent()) {
					return ResponseEntity.ok(tournament.get().getRounds());
			}else{
				return ResponseEntity.notFound().build();				
			}
		}
	    
	    @GetMapping("/{tournamentId}/rounds/{roundId}")
	    @JsonView(RoundDetails.class)
		public ResponseEntity<Rounds> getTournamentRound( @PathVariable Long tournamentId, @PathVariable Long roundId){
			Optional<Tournament> tournament = tournamentService.findById(tournamentId);
			Optional <Rounds> round = roundsService.findById(roundId);
			
			if(tournament.isPresent() && round.isPresent() && tournament.get().getRounds().contains(round.get())) {
				return ResponseEntity.ok(tournament.get().getRounds().get(tournament.get().getRounds().indexOf(round.get())));
					
			}else{
				return ResponseEntity.notFound().build();				
			}
		}
	    
	    @PostMapping("/{tournamentId}/")
	    public ResponseEntity<Collection<Rounds>> createRounds(@RequestBody List <Rounds> rounds, @PathVariable  Long tournamentId){
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId); 
	    	
	    	if (tournament.isPresent()) {
	    		tournament.get().setRounds(rounds);
	    		tournamentService.saveTournament(tournament.get());
	    		
	    		URI location = fromCurrentRequest().path("/rounds").buildAndExpand().toUri();
	    		return ResponseEntity.created(location).body(rounds);
	    	
	    	}else{
				return ResponseEntity.notFound().build();				
	    	}
	    	
	    }
	    
	    @PostMapping("/{tournamentId}/rounds/")
	    public ResponseEntity<Rounds> createRound(@RequestBody Rounds round,
	    		@PathVariable  Long tournamentId, 
	    		@PathVariable  int roundNumber){
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId); 
	    	
	    	if (tournament.isPresent() ) {
	    		//MEM
	    		tournament.get().getRounds().add(round);
	    		tournamentService.saveTournament(tournament.get());
	    		
	    		URI location = fromCurrentRequest().path("/{roundId}")
	    				.buildAndExpand(round.getId()).toUri();
	    		return ResponseEntity.created(location).body(round);
	    	
	    	}else{
				return ResponseEntity.notFound().build();				
	    	}
	    	
	    }
	    
	    @PutMapping("/{tournamentId}/rounds")
	    public ResponseEntity < Collection <Rounds> > replaceRounds(@PathVariable Long tournamentId, @RequestBody List<Rounds> newRounds ){
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId);
	    	
	    	if (tournament.isPresent()) {
	    		tournament.get().setRounds(newRounds);
	    		tournamentService.saveTournament(tournament.get());
	    		return ResponseEntity.ok(tournament.get().getRounds());
	    		
	    	}else{
	    		return ResponseEntity.notFound().build();
	    	}
	    }
	    
	    @PutMapping("/{tournamentId}/rounds/{roundId}")
	    public ResponseEntity <Rounds>  replaceRounds(@PathVariable Long tournamentId,
	    		@PathVariable Long roundId,
	    		@RequestBody Rounds newRound ){
	    	int roundIndex; 
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId);
	    	Optional <Rounds> round = roundsService.findById(roundId);
	    	
	    	if (tournament.isPresent() && round.isPresent() && tournament.get().getRounds().contains(round.get()) && newRound.getId() == round.get().getId() ) {
	    		List <Rounds> rounds = tournament.get().getRounds();
	    		roundIndex = rounds.indexOf(round.get());
	    		rounds.remove(roundIndex);
	    		rounds.set(roundIndex, newRound);
	    		tournamentService.saveTournament(tournament.get());
	    		
	    		return ResponseEntity.ok(tournament.get().getRounds().get(roundIndex));
	    		
	    	}else{
	    		return ResponseEntity.notFound().build();
	    	}
	    }
	    
	    @GetMapping("/{tournamentId}/rounds/{roundId}/matches")
	    @JsonView(MatchDetails.class)
	    public ResponseEntity < Collection <MatchUp> > getRoundMatches (@PathVariable Long tournamentId,
	    		@PathVariable Long roundId ){
	    	int roundIndex;
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId);
	    	Optional <Rounds> round = roundsService.findById(roundId);
			
	    	if(tournament.isPresent() && round.isPresent() && tournament.get().getRounds().contains(round.get())) {
	    		List <Rounds> rounds = tournament.get().getRounds();
				roundIndex = rounds.indexOf(round.get());
	    		
				return ResponseEntity.ok(tournament.get().getRounds().get(roundIndex).getMatches());

	    		
	    	}else{
	    		return ResponseEntity.notFound().build();

	    	}
	    	
	    }
	    
	    @GetMapping("{tournamentId}/rounds/{roundId}/matches/{matchId}")
	    @JsonView(MatchDetails.class)
	    public ResponseEntity <MatchUp> getRoundMatch(@PathVariable Long tournamentId,
	    		@PathVariable Long roundId,
	    		@PathVariable Long matchId){
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId);
	    	Optional <Rounds> round = roundsService.findById(roundId);
			Optional <MatchUp> match = matchService.findById(matchId);                                                                                                      			
	    	if(tournament.isPresent() && round.isPresent() &&  match.isPresent() 
	    			&& tournament.get().getRounds().contains(round.get()) 
	    			&& round.get().getMatches().contains(match.get()) ) {
	    		
	    		return ResponseEntity.ok(match.get());

	    		
	    	}else{
	    		return ResponseEntity.notFound().build();
	    	}
	    }
		
	    @PostMapping("/{tournamentId}/rounds/{roundId}")
	    public ResponseEntity<Collection <MatchUp>> createMatches(@RequestBody List <MatchUp> newMatches,
	    		@PathVariable Long tournamentId,
	    		@PathVariable Long roundId){
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId); 
	    	Optional <Rounds> round = roundsService.findById(roundId); 
	    	if(tournament.isPresent() && round.isPresent() && tournament.get().getRounds().contains(round.get())){
	    		int roundIndex = 	tournament.get().getRounds().indexOf(round.get());
	    		round.get().setMatches(newMatches);
	    		tournament.get().getRounds().remove(roundIndex);
	    		tournament.get().getRounds().add(roundIndex, round.get());
	    		roundsService.saveRounds(round.get());
	    		tournamentService.saveTournament(tournament.get());
		    	
		    	URI location = fromCurrentRequest().path("/maches").buildAndExpand().toUri(); 
		    	return ResponseEntity.created(location).body(newMatches);
	    	}else {
	    		return ResponseEntity.notFound().build();
	    	}
	    	
	    }
	    
	    @PostMapping("/{tournamentId}/rounds/{roundId}/matches")
	    public ResponseEntity<MatchUp> createMatch(@RequestBody MatchUp newMatch,
	    		@PathVariable Long tournamentId,
	    		@PathVariable Long roundId){
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId); 
	    	Optional <Rounds> round = roundsService.findById(roundId); 
	    	if(tournament.isPresent() && round.isPresent() && tournament.get().getRounds().contains(round.get())){
	    		int roundIndex = 	tournament.get().getRounds().indexOf(round.get());
	    		List <MatchUp> matches = tournament.get().getRounds().get(roundIndex).getMatches();
	    		matchService.saveMatch(newMatch);
	    		matches.add(newMatch);
	    		tournament.get().getRounds().get(roundIndex).setMatches(matches);
	    		tournamentService.saveTournament(tournament.get());
		    	
		    	URI location = fromCurrentRequest().path("/{matchId}").buildAndExpand(newMatch.getId()).toUri(); 
		    	return ResponseEntity.created(location).body(newMatch);
	    	}else {
	    		return ResponseEntity.notFound().build();
	    	}
	    	
	    }
	    
	    @PutMapping("/{tournamentId}/rounds/{roundId}/matches")
	    public ResponseEntity<Collection <MatchUp>> updateMatches(@RequestBody List <MatchUp> newMatches,
	    		@PathVariable Long tournamentId,
	    		@PathVariable Long roundId){
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId); 
	    	Optional <Rounds> round = roundsService.findById(roundId); 
	    	if(tournament.isPresent() && round.isPresent() && tournament.get().getRounds().contains(round.get()) ){
	    		int roundIndex = 	tournament.get().getRounds().indexOf(round.get());
	    		tournament.get().getRounds().get(roundIndex).setMatches(newMatches);
	    		tournamentService.saveTournament(tournament.get());
		 		return ResponseEntity.ok(newMatches);
	    	}else {
	    		return ResponseEntity.notFound().build();
	    	}
	    	
	    }
	    
	    @PutMapping("/{tournamentId}/rounds/{roundId}/matches/{matchId}")
	    public ResponseEntity <MatchUp> updateMatches(@RequestBody MatchUp newMatch,
	    		@PathVariable Long tournamentId,
	    		@PathVariable Long roundId,
	    		@PathVariable Long matchId){
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId); 
	    	Optional <Rounds> round = roundsService.findById(roundId);
	    	Optional <MatchUp> match = matchService.findById(matchId);
	    	if(tournament.isPresent() && round.isPresent() && 
	    			tournament.get().getRounds().contains(round.get()) &&
	    			round.get().getMatches().contains(match.get())){
	    		int roundIndex = 	tournament.get().getRounds().indexOf(round.get());
	    		int matchIndex = tournament.get().getRounds().get(roundIndex).getMatches().indexOf((match.get()));
	    		tournament.get().getRounds().get(roundIndex).getMatches().remove(match.get());
	    		tournament.get().getRounds().get(roundIndex).getMatches().add(matchIndex, newMatch);
	    		tournamentService.saveTournament(tournament.get());
		 		return ResponseEntity.ok(newMatch);
	    	}else {
	    		return ResponseEntity.notFound().build();
	    	}
	    	
	    }
	        
}
