package com.victorious.tournament;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.victorious.game.Game;
import com.victorious.game.GameService;
import com.victorious.team.Team;
import com.victorious.team.TeamService;
import com.victorious.user.User;
import com.victorious.user.UserService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/tournaments")
public class TournamentRestController {
		
		interface TournamentDetails extends Tournament.Basic, Tournament.TournamentTeams, Tournament.TournamentRounds, 
		Team.Basic, Game.Basic, Rounds.Basic {}
		interface RoundDetails extends Rounds.Basic, Rounds.RoundMatchUps, Team.Basic, MatchUp.Basic, Game.Basic {}
		interface MatchDetails extends MatchUp.Basic, MatchUp.MatchUpTeams, Team.Basic, Game.Basic {}
	
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
		@JsonView(TournamentDetails.class)
		public ResponseEntity <Collection<Tournament>> getTournametPage (@RequestParam int numPage){
			Pageable webPage = PageRequest.of(numPage, 4);
			
			Page <Tournament> tournamentPage = tournamentService.findAll(webPage);
			
			if(tournamentPage.getContent().isEmpty()){
	            return ResponseEntity.notFound().build();
	        }
			
			return ResponseEntity.ok(tournamentPage.getContent());
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
	
		@PostMapping("/")
		@JsonView(TournamentDetails.class)
		public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament){
			if (!tournamentService.findByName(tournament.getName()).isPresent()) {			
				String gameName = tournament.getGame().getName();
				Game tgame = gameService.findByName(gameName).get(); 
				tournament.setGame(tgame);
				Tournament newTournament = new Tournament(tournament.getName(),tournament.getDescription(), tournament.getMaxPlayers(),
						tournament.getIniDate(),tournament.getEndDate(), tgame);
				
				tournamentService.saveTournament(newTournament);
				
				URI location = fromCurrentRequest().path("/{id}")
						.buildAndExpand(tournament.getId()).toUri();
				
				return ResponseEntity.created(location).body(newTournament);

			}else{
			
				return ResponseEntity.notFound().build();
			}
		}
			
		@PutMapping("/{id}")
		@JsonView(TournamentDetails.class)
		public ResponseEntity<Tournament> replaceTournament(@PathVariable long id,
			@RequestBody Tournament newTournament, HttpServletRequest request){
			Principal principal = request.getUserPrincipal();
			String userLoggedName = principal.getName();
			Optional<User> userLogged = userService.findByName(userLoggedName);
			
			Optional <Tournament> tournament = tournamentService.findById(id);
			
	    	boolean isAdmin = (((tournament.get().getAdmin() != null) && (tournament.get().getAdmin().equals(userLogged.get()))) || (userLogged.get().getRoles().contains("ADMIN")));
			if (tournament.isPresent() && isAdmin) {
				newTournament.setId(id);
				tournamentService.saveTournament(newTournament);
				
				return ResponseEntity.ok(tournament.get());
				
			}else{
				
				return ResponseEntity.notFound().build();
			}
			
		}
		
		@GetMapping("/{tournamentId}/participants")
		@JsonView(TournamentDetails.class)
		public ResponseEntity< Collection<Team> > getTournamentParticipants(@PathVariable Long tournamentId){
			Optional<Tournament> tournament = tournamentService.findById(tournamentId);

			if(tournament.isPresent()) {
				return ResponseEntity.ok(tournament.get().getParticipants());
			}else{
				return ResponseEntity.notFound().build();				
			}
		}

		@GetMapping("/{tournamentId}/participants/{teamId}")
		@JsonView(TournamentDetails.class)
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
	    @JsonView(TournamentDetails.class)
		public ResponseEntity<Team> addParticipantToTournament(@PathVariable Long tournamentId, HttpServletRequest request){
	    	Principal principal = request.getUserPrincipal();
			String userLoggedName = principal.getName();
			Optional<User> userLogged = userService.findByName(userLoggedName);
			
			Optional<Tournament> tournament = tournamentService.findById(tournamentId);
			Optional<Team> teamAdd = teamService.findByName(userLogged.get().getTeam().getName());
			
			if(!tournament.get().getParticipants().contains(teamAdd.get())) {
				tournament.get().addTeam(teamAdd.get());
				teamAdd.get().addTournament(tournament.get());
				teamService.saveTeam(teamAdd.get());
				tournamentService.saveTournament(tournament.get());
				
				URI location = fromCurrentRequest().path("/{id}")
						.buildAndExpand(teamAdd.get().getId()).toUri();
					
				return ResponseEntity.created(location).body(teamAdd.get());
				
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
	    
	    @PostMapping("/{tournamentId}/rounds")
	    @JsonView(RoundDetails.class)
	    public ResponseEntity<Rounds> createRounds(@PathVariable  Long tournamentId, HttpServletRequest request){
	    	Principal principal = request.getUserPrincipal();
			String userLoggedName = principal.getName();
			Optional<User> userLogged = userService.findByName(userLoggedName);
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId); 
	    	
	    	boolean isAdmin = (((tournament.get().getAdmin() != null) && (tournament.get().getAdmin().equals(userLogged.get()))) || (userLogged.get().getRoles().contains("ADMIN")));
	    	if (tournament.isPresent() && isAdmin) {
	    		if(!tournament.get().isStarted()) {
	    			List<Team> participants = new ArrayList<Team>(tournament.get().getParticipants());
	    			Collections.shuffle(participants);
	    			Rounds round = new Rounds(participants);
	    			if(!round.isEvenRound()){
	    				round.setOddRound();
	    			}
	    			//
	    			int i=0;
	    			while(i<round.getParticipants().size()){
	    				MatchUp matchUp = new MatchUp(round.getParticipants().get(i), round.getParticipants().get(i+1));
	    				matchService.saveMatch(matchUp);
	    				round.addMatch(matchUp);
	    				i=i+2;
	    			}
	    			//
	    			roundsService.saveRounds(round);
	    			tournament.get().addRound(round);
	    			round.setNumRound(tournament.get().getRoundNumber());
	    			roundsService.saveRounds(round);
	    			tournament.get().setStarted(true);
	    			tournamentService.saveTournament(tournament.get());
	    			
	    			URI location = fromCurrentRequest().path("/{id}").buildAndExpand(round.getId()).toUri();
		    		return ResponseEntity.created(location).body(round);
		    		
	    		}else if(tournament.get().isFinished() == false){
	    			int j = 0;
	    			while(j<tournament.get().getRounds().get(tournament.get().getRoundNumber()-1).getMatches().size()){
	    				tournament.get().getRounds().get(tournament.get().getRoundNumber()-1).getMatches().get(j).setPlayed(true);
	    				j++;
	    			}
	    			List<Team> participants = new ArrayList<Team>(tournament.get().getRounds().get(tournament.get().getRoundNumber()-1).getWinners());
	    			if(participants.size()>1){
	    				Collections.shuffle(participants);
	    				Rounds round = new Rounds(participants);
	    				if(!round.isEvenRound()){
	    					round.setOddRound();
	    				}
	    				//
	    				int i=0;
	    				while(i<round.getParticipants().size()){
	    					MatchUp matchUp = new MatchUp(round.getParticipants().get(i), round.getParticipants().get(i+1));
	    					matchService.saveMatch(matchUp);
	    					round.addMatch(matchUp);
	    					i=i+2;
	    				}
	    				//
	    				roundsService.saveRounds(round);
	    				tournament.get().addRound(round);
	    				round.setNumRound(tournament.get().getRoundNumber());
	    				roundsService.saveRounds(round);
	    				tournamentService.saveTournament(tournament.get());
	    				
	    				URI location = fromCurrentRequest().path("/{id}").buildAndExpand(round.getId()).toUri();
	    	    		return ResponseEntity.created(location).body(round);
	    	    		
	    			}else {
	    				tournament.get().setFinished(true);
	    				tournament.get().setWinner(participants.get(0));
	    				tournamentService.saveTournament(tournament.get());
	    				
	    				return ResponseEntity.ok().build();
	    			}	    
	    		} else {
	    			return ResponseEntity.badRequest().build();	
	    		}
	    	}else {
				return ResponseEntity.badRequest().build();				
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
	    public ResponseEntity <MatchUp> getRoundMatch(@PathVariable Long tournamentId, @PathVariable Long roundId, @PathVariable Long matchId){			
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
	    
	    @PutMapping("/{tournamentId}/rounds/{roundId}/matches/{matchId}")
	    @JsonView(MatchDetails.class)
	    public ResponseEntity <MatchUp> updateMatch(@RequestBody MatchUp matchUp, @PathVariable Long tournamentId,
	    		@PathVariable Long matchId, HttpServletRequest request){
	    	Principal principal = request.getUserPrincipal();
			String userLoggedName = principal.getName();
			Optional<User> userLogged = userService.findByName(userLoggedName);
			
	    	Optional <Tournament> tournament = tournamentService.findById(tournamentId); 
	    	Optional <MatchUp> match = matchService.findById(matchId);
	    	
	    	boolean isAdmin = (((tournament.get().getAdmin() != null) && (tournament.get().getAdmin().equals(userLogged.get()))) || (userLogged.get().getRoles().contains("ADMIN")));
	    	if(isAdmin && !match.get().isPlayed()){
				match.get().setScore1(matchUp.getScore1());
				match.get().setScore2(matchUp.getScore2());
				match.get().setPlayed(true);
				matchService.saveMatch(match.get());
				tournamentService.saveTournament(tournament.get());	    	
		 		return ResponseEntity.ok(match.get());
	    	}else {
	    		return ResponseEntity.notFound().build();
	    	}
	    	
	    }
	        
}
