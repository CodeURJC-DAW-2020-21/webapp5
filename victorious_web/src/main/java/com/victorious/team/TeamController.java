package com.victorious.team;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.victorious.game.Game;
import com.victorious.game.GameService;

@Controller
public class TeamController {
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	GameService gameService;
	
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
	
	@RequestMapping("/teams")
	public String Teams(Model model, @PageableDefault(size = 4) Pageable pageable) {
		Page<Team> teamPages = teamService.findAll(pageable);
		model.addAttribute("teams", teamPages);
		
		model.addAttribute("nextPage", teamPages.getNumber() + 1);
        model.addAttribute("prevPage", teamPages.getNumber() - 1);
        model.addAttribute("numPage", teamPages.getNumber());
        model.addAttribute("showNext", !teamPages.isLast());
        model.addAttribute("showPrev", !teamPages.isFirst());
        model.addAttribute("hasNext", teamPages.hasNext());
        model.addAttribute("hasPrevious", teamPages.hasPrevious());
        
		return "teams";
	}
	
	@GetMapping("teams/teamPages")
	public ResponseEntity<String> getPagesList(@RequestParam() int pageId){
		String result = "";
		Page<Team> pageTeam = teamService.findAll(PageRequest.of(pageId, 4)) ;
		if(pageTeam.isLast()) 
			result += "nomore";
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/teams/{id}")
	public String showTeam(Model model, @PathVariable Long id) {
		
		Optional<Team> team = teamService.findById(id);

		if(team.isPresent()) {
			model.addAttribute("actualTeam", team.get());
		}
		
		List<Game> games = gameService.findAll();
		for (Game g : team.get().getGames()) {
			games.remove(g);
		}
		model.addAttribute("games", games);
		
		//examples for stats 
				model.addAttribute("victories", "0,1,2,2,3,4");
				model.addAttribute("loses",     "1,1,1,2,2,2");
				model.addAttribute("matches",   "1,2,3,4,5,6");

		return "team";
	}
	
	@GetMapping("/teams/{id}/image")
	public ResponseEntity<Object> downloadImage(@PathVariable Long id) throws SQLException {

		Optional<Team> team = teamService.findById(id);
		if (team.isPresent() && team.get().getImageFile() != null) {

			Resource file = new InputStreamResource(team.get().getImageFile().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(team.get().getImageFile().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/teams/{id}/changeImage")
	public String changeImage(Model model, @PathVariable Long id, MultipartFile imageField) throws IOException, SQLException {
		Optional<Team> team = teamService.findById(id);
		updateImage(team.get(), imageField);
		teamService.saveTeam(team.get());
		model.addAttribute("actualTeam", team.get());
		return "redirect:/teams/"+ team.get().getId();
	}
	
	@GetMapping("/newTeam")
	public String newTeam(Model model, @RequestParam(required = false) boolean error) {
		model.addAttribute("error", error);
		return "newTeam";
	}
	
	@PostMapping("/newTeam")
	public View createTeam(Model model, @RequestParam String name, @RequestParam String description) throws IOException {
		RedirectView rv;
		if (!teamService.findByName(name).isPresent()) {
			Team team = new Team(name, description);
			setTeamImage(team, "/sample_images/team_default.jpg");
			teamService.createTeam(team);
			rv = new RedirectView("teams");
		} else {
			rv = new RedirectView("/newTeam?error=true");
		}
		return rv;
	}
	
	private void setTeamImage(Team team, String classPathResource) throws IOException {
		team.setImage(true);
		Resource image = new ClassPathResource(classPathResource);
		team.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
	}
	
private void updateImage(Team team, MultipartFile imageField) throws IOException, SQLException {
		
		team.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
		team.setImage(true);
	
		// Maintain the same image loading it before updating the book
		Team dbTeam = teamService.findById(team.getId()).orElseThrow();
		if (dbTeam.hasImage()) {
			team.setImageFile(BlobProxy.generateProxy(dbTeam.getImageFile().getBinaryStream(),
					dbTeam.getImageFile().length()));
			team.setImage(true);
		}
	}
}
