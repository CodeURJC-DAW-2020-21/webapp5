package com.victorious.tournament;


import java.util.Optional;

import com.victorious.game.Game;
import com.victorious.game.GameRepository;

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
	GameRepository gameRepository;

    @RequestMapping("/tournaments")
	public String Tournaments(Model model, @PageableDefault(size = 4) Pageable pageable) {
		Page<Tournament> tournamentPages = tournamentService.findAll(pageable);
		model.addAttribute("tournaments", tournamentPages);
		
		model.addAttribute("nextPage", tournamentPages.getNumber() + 1);
        model.addAttribute("prevPage", tournamentPages.getNumber() - 1);
        model.addAttribute("numPage", tournamentPages.getNumber());
        model.addAttribute("showNext", !tournamentPages.isLast());
        model.addAttribute("showPrev", !tournamentPages.isFirst());
        model.addAttribute("hasNext", tournamentPages.hasNext());
        model.addAttribute("hasPrevious", tournamentPages.hasPrevious());
        
		
		return "tournaments";
	}	
	
	@RequestMapping("/tournaments/tournament")
	public String tournament(Model model){
		
		return "tournament";
	}
	
	@GetMapping("tournament/tournamentPages")
	public ResponseEntity<String> getPagesList(@RequestParam() int pageId){
		String result = "";
		Page<Tournament> pageTournament = tournamentService.findAll(PageRequest.of(pageId, 4)) ;
		if(pageTournament.isLast()) 
			result += "nomore";
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/tournaments/{id}")
	public String showTournament(Model model, @PathVariable Long id) {
		
		Optional<Tournament> tournament = tournamentService.findById(id);

		if(tournament.isPresent()) {
			model.addAttribute("actualTournament", tournament.get());
		}

		return "tournament";
	}

	@GetMapping("/newTournament")
	public String newTournament(Model model, @RequestParam(required = false) boolean error) {
		model.addAttribute("error", error);
		return "newTournament";
	}

	@PostMapping("/newTournament")
	public View createTournament(Model model, @RequestParam String name, @RequestParam String description, @RequestParam int maxPlayers, @RequestParam String iniDate, @RequestParam String endDate, @RequestParam String gameName) {
		RedirectView rv;
		if (!tournamentService.findByName(name).isPresent()) {
			Game game = gameRepository.findByName(gameName).get();
			Tournament tournament = new Tournament(name, description, maxPlayers, iniDate, endDate, game);
			tournamentService.createTournament(tournament);
			rv = new RedirectView("tournaments");
		} else {
			rv = new RedirectView("/newTournament?error=true");
		}
		return rv;
	}
}
