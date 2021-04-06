package com.victorious.tournament;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.victorious.game.Game;
import com.victorious.game.GameService;
import com.victorious.team.Team;
import com.victorious.team.TeamService;
import com.victorious.user.User;
import com.victorious.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class TournamentController {
    
    @Autowired
    TournamentService tournamentService;

	@Autowired
	GameService gameService;

	@Autowired
	RoundsService roundService;

	@Autowired
	MatchService matchService;

	@Autowired
	UserService userService;
	
	@Autowired 
	TeamService teamService; 

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

    @RequestMapping("/tournaments")
	public String Tournaments(Model model, @PageableDefault(size = 4) Pageable pageable, @RequestParam(required = false) boolean joinError) {
		Page<Tournament> tournamentPages = tournamentService.findAll(pageable);
		model.addAttribute("tournaments", tournamentPages);
		model.addAttribute("joinError", joinError);
		
		model.addAttribute("nextPage", tournamentPages.getNumber() + 1);
        model.addAttribute("prevPage", tournamentPages.getNumber() - 1);
        model.addAttribute("numPage", tournamentPages.getNumber());
        model.addAttribute("showNext", !tournamentPages.isLast());
        model.addAttribute("showPrev", !tournamentPages.isFirst());
        model.addAttribute("hasNext", tournamentPages.hasNext());
        model.addAttribute("hasPrevious", tournamentPages.hasPrevious());
 
		return "tournaments";
	}	
	
	@GetMapping("tournaments/tournamentPages")
	public ResponseEntity<String> getPagesList(@RequestParam() int pageId){
		String result = "";
		Page<Tournament> pageTournament = tournamentService.findAll(PageRequest.of(pageId, 4)) ;
		if(pageTournament.isLast()) 
			result += "nomore";
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/tournaments/{id}")
	public String showTournament(Model model, @PathVariable Long id, HttpServletRequest request, @RequestParam(required = false) boolean tournamentError) {
		
		Optional<Tournament> tournament = tournamentService.findById(id);
		Principal principal = request.getUserPrincipal();

		model.addAttribute("tournamentError", tournamentError);

		if(tournament.isPresent()) {
			model.addAttribute("actualTournament", tournament.get());
		}
		if (principal != null ) {
			User user = userService.findByName(principal.getName()).get();
			if(((tournament.get().getAdmin() != null) && (tournament.get().getAdmin().equals(user))) || (user.getRoles().contains("ADMIN"))){
				model.addAttribute("tournamentAdmin", true);
			}
			model.addAttribute("joined", tournament.get().getParticipants().contains(user.getTeam()));

		}
		return "tournament";
	}

	@GetMapping("/newTournament")
	public String newTournament(Model model, @RequestParam(required = false) boolean error) {
		model.addAttribute("error", error);
        model.addAttribute("games", gameService.findAll());
		return "newTournament";
	}

	@PostMapping("/newTournament")
	public View createTournament(Model model, @RequestParam String name, @RequestParam String description, @RequestParam int maxPlayers, @RequestParam String iniDate, @RequestParam String endDate, @RequestParam String gameName) {
		RedirectView rv;
		String userCreatorName = (String) model.getAttribute("userName");
		User user = userService.findByName(userCreatorName).get();
		if (!tournamentService.findByName(name).isPresent()) {
			Game game = gameService.findByName(gameName).get();
			Tournament tournament = new Tournament(name, description, maxPlayers, iniDate, endDate, game);
			tournament.setAdmin(user);
			tournamentService.createTournament(tournament);
			rv = new RedirectView("tournaments");
		} else {	
			rv = new RedirectView("/newTournament?error=true");
		}
		return rv;
	}
	
	@GetMapping("/tournaments/{id}/join")
	public View addTeam(@PathVariable Long id, Model model, HttpServletRequest request ) {
		RedirectView rv;
		Tournament tournament = tournamentService.findById(id).get();
		String userName = (String) model.getAttribute("userName");      
		User user = userService.findByName(userName).get();   
		if ((boolean) model.getAttribute("logged") && tournament != null && user.getTeam() != null && !tournament.getParticipants().contains(user.getTeam())) {
			tournament.addTeam(user.getTeam());
			user.getTeam().addTournament(tournament);
			teamService.saveTeam(user.getTeam());
			tournamentService.saveTournament(tournament);
			rv = new RedirectView("/tournaments");
		}else{
			rv = new RedirectView("/tournaments?joinError=true");
		}
		rv.setExposeModelAttributes(false);
		return rv;  
	}
	
	@PostMapping("/tournaments/{id}/start")
	public View startTournament(Model model, @PathVariable Long id) {
		RedirectView rv;
		Tournament tournament = tournamentService.findById(id).get();
		String userName = (String) model.getAttribute("userName");
		User loggedUser = userService.findByName(userName).get();
		boolean isAdmin = (((tournament.getAdmin() != null) && (tournament.getAdmin().equals(loggedUser))) || (loggedUser.getRoles().contains("ADMIN")));
		if (!tournament.isStarted() && tournament.getParticipants().size()>1 && isAdmin){
			List<Team> participants = new ArrayList<Team>(tournament.getParticipants());
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
			roundService.saveRounds(round);
			tournament.addRound(round);
			round.setNumRound(tournament.getRoundNumber());
			roundService.saveRounds(round);
			tournament.setStarted(true);
			tournamentService.saveTournament(tournament);

			rv= new RedirectView("/tournaments/" + tournament.getId());
		}else{
			rv= new RedirectView("/tournaments/" + tournament.getId() + "?tournamentError=true");
		}
		rv.setExposeModelAttributes(false);
		return rv;
	}

	@PostMapping("/tournament/{id}/submitScore")
	public View submitScore(Model model, @PathVariable Long id, @RequestParam Long idMatch, @RequestParam int score1, @RequestParam int score2){
		RedirectView rv;
		Tournament tournament = tournamentService.findById(id).get();
		MatchUp matchUp = matchService.findById(idMatch).get();
		String userName = (String) model.getAttribute("userName");
		User loggedUser = userService.findByName(userName).get();
		boolean isAdmin = (((tournament.getAdmin() != null) && (tournament.getAdmin().equals(loggedUser))) || (loggedUser.getRoles().contains("ADMIN")));
		if(isAdmin){
			matchUp.setScore1(score1);
			matchUp.setScore2(score2);
			matchUp.setPlayed(true);
			matchService.saveMatch(matchUp);
			tournamentService.saveTournament(tournament);
			rv= new RedirectView("/tournaments/" + tournament.getId());
		}else{
			rv= new RedirectView("/tournaments/" + tournament.getId() + "?tournamentError=true");
		}
		rv.setExposeModelAttributes(false);
		return rv;
	}

	@PostMapping("/tournament/{id}/nextRound")
	public View nextRound(Model model, @PathVariable Long id){
		RedirectView rv;
		Tournament tournament = tournamentService.findById(id).get();
		String userName = (String) model.getAttribute("userName");
		User loggedUser = userService.findByName(userName).get();
		boolean isAdmin = (((tournament.getAdmin() != null) && (tournament.getAdmin().equals(loggedUser))) || (loggedUser.getRoles().contains("ADMIN")));
		int j=0;
		if(isAdmin){
			while(j<tournament.getRounds().get(tournament.getRoundNumber()-1).getMatches().size()){
				tournament.getRounds().get(tournament.getRoundNumber()-1).getMatches().get(j).setPlayed(true);
				j++;
			}
			List<Team> participants = new ArrayList<Team>(tournament.getRounds().get(tournament.getRoundNumber()-1).getWinners());
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
				roundService.saveRounds(round);
				tournament.addRound(round);
				round.setNumRound(tournament.getRoundNumber());
				roundService.saveRounds(round);
				tournamentService.saveTournament(tournament);
			}else{
				tournament.setFinished(true);
				tournament.setWinner(participants.get(0));
				tournamentService.saveTournament(tournament);

			}
			rv= new RedirectView("/tournaments/" + tournament.getId());
		}else{
			rv= new RedirectView("/tournaments/" + tournament.getId() + "?tournamentError=true");
		}
		rv.setExposeModelAttributes(false);
		return rv;
	}
}
