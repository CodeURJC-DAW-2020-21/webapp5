package com.victorious.team;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
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
public class TeamController {
	
	@Autowired
	TeamService teamService;
	
	@RequestMapping("/teams")
	public String Teams(Model model, @PageableDefault(size = 4) Pageable pageable) {
		Page<Team> teamPages = teamService.findAll(pageable);
		model.addAttribute("teams", teamPages);
		
		model.addAttribute("Next", teamPages.getNumber() + 1);
        model.addAttribute("Last", teamPages.getNumber() - 1);
        model.addAttribute("showNext", !teamPages.isLast());
        model.addAttribute("showPrev", !teamPages.isFirst());
        
		return "teams";
	}
	
	@GetMapping("/teams/{id}")
	public String showTeam(Model model, @PathVariable Long id) {
		
		Optional<Team> team = teamService.findById(id);

		if(team.isPresent()) {
			model.addAttribute("actualTeam", team.get());
		}

		return "team";
	}
	
	@GetMapping("/newTeam")
	public String newTeam(Model model, @RequestParam(required = false) boolean error) {
		model.addAttribute("error", error);
		return "newTeam";
	}
	
	@PostMapping("/newTeam")
	public View createTeam(Model model, @RequestParam String name, @RequestParam String description) {
		RedirectView rv;
		if (!teamService.findByName(name).isPresent()) {
			Team team = new Team(name, description);
			teamService.createTeam(team);
			rv = new RedirectView("teams");
		} else {
			rv = new RedirectView("/newTeam?error=true");
		}
		return rv;
	}
}
