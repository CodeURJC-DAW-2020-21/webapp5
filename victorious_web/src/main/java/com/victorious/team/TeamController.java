package com.victorious.team;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.victorious.user.User;
import com.victorious.user.UserService;

@Controller
public class TeamController {
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	GameService gameService;
	
	@Autowired
	UserService userService;
	
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
	public String showTeam(Model model, HttpServletRequest request, @PathVariable Long id, @RequestParam(required = false) boolean teamError, 
			@RequestParam(required = false) boolean teamRequest) {
		
		Optional<Team> team = teamService.findById(id);
		Principal principal = request.getUserPrincipal();
		
		model.addAttribute("teamError", teamError);
		model.addAttribute("teamRequest", teamRequest);

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
				
		if (principal != null) {
			User user = userService.findByName(principal.getName()).get();
			model.addAttribute("teamAdmin", team.get().isAdmin(user) || user.getRoles().contains("ADMIN"));
			List<User> requests = new ArrayList<>();
			for (Long userId : team.get().getRequests()) {
				requests.add(userService.findById(userId).get());
			}
			if (user.getTeam() == null && !team.get().getRequests().contains(user.getId())) {
				model.addAttribute("doRequest", true);
			} else {
				if (team.get().getUsers().contains(user)) {
					model.addAttribute("leaveTeam", true);
				}
			}
			model.addAttribute("requests", requests);
		}

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
		String userCreatorName = (String) model.getAttribute("userName");
		User user = userService.findByName(userCreatorName).get();
		if (!teamService.findByName(name).isPresent()) {
			Team team = new Team(name, description);
			setTeamImage(team, "/sample_images/team_default.jpg");
			team.getAdmins().add(user);
			team.setCreator(user);
			teamService.createTeam(team);
			rv = new RedirectView("teams");
		} else {
			rv = new RedirectView("/newTeam?error=true");
		}
		rv.setExposeModelAttributes(false);
		return rv;
	}
	
	@PostMapping("/teams/{id}/newRequest")
	public View newRequest(Model model, @PathVariable Long id, @RequestParam String userName) {

		Team team = teamService.findById(id).get();
		User user = userService.findByName(userName).get();
		Long userId = user.getId();

		RedirectView rv;
		if (!team.getRequests().contains(userId)) {
			team.getRequests().add(userId);
			rv = new RedirectView("/teams/" + id + "?teamRequest=true");
		} else {
			rv = new RedirectView("/teams/" + id + "?teamError=true");
		}
		teamService.saveTeam(team);
		rv.setExposeModelAttributes(false);
		return rv;
	}
	
	@PostMapping("teams/{id}/leaveTeam")
	public View leaveTeam(Model model, @PathVariable Long id, @RequestParam String userName) {
		User user = userService.findByName(userName).get();
		Team team = teamService.findById(id).get();
		RedirectView rv;
		if (user.getTeam() != null && user.getTeam().equals(team)) {
			if (team.isAdmin(user)) {
				team.getAdmins().remove(user);
			}
			user.setTeam(null);
			rv = new RedirectView("/teams/" + team.getId());
		} else {
			rv = new RedirectView("/teams/" + team.getId() + "?teamError=true");
		}
		userService.saveUser(user);
		rv.setExposeModelAttributes(false);
		return rv;
	}

	
	@PostMapping("teams/{id}/kickUser")
	public View kickUser(Model model, @PathVariable Long id, @RequestParam Long userId) {

		String userName = (String) model.getAttribute("userName");
		User loggedUser = userService.findByName(userName).get();
		User user = userService.findById(userId).get();
		Team team = teamService.findById(id).get();
		boolean isAdmin = team.isAdmin(loggedUser) || loggedUser.getRoles().contains("ADMIN") || user.equals(loggedUser);
		RedirectView rv;
		if (user.getTeam() != null && user.getTeam().equals(team) && isAdmin) {
			if (team.isAdmin(user)) {
				team.getAdmins().remove(user);
			}
			user.setTeam(null);
			rv = new RedirectView("/teams/" + team.getId());
		} else {
			rv = new RedirectView("/teams/" + team.getId() + "?teamError=true");
		}
		userService.saveUser(user);
		rv.setExposeModelAttributes(false);
		return rv;
	}
	
	@PostMapping("/teams/{id}/addAdmin")
	public View addAdmin(Model model, @PathVariable Long id, @RequestParam Long userId) {

		String userName = (String) model.getAttribute("userName");
		User loggedUser = userService.findByName(userName).get();
		User user = userService.findById(userId).get();
		Team team = teamService.findById(id).get();
		boolean isAdmin = team.isAdmin(loggedUser) || loggedUser.getRoles().contains("ADMIN");
		RedirectView rv;
		if (!team.isAdmin(user) && user.getTeam().equals(team) && isAdmin) {
			team.addAdmin(user);
			rv = new RedirectView("/teams/" + team.getId());
		} else {
			rv = new RedirectView("/teams/" + team.getId() + "?teamError=true");
		}
		teamService.saveTeam(team);
		rv.setExposeModelAttributes(false);
		return rv;
	}
	
	@PostMapping("/teams/{id}/acceptRequest")
	public View addUser(Model model, @PathVariable Long id, @RequestParam Long userId,
			@RequestParam boolean accept) {

		String userName = (String) model.getAttribute("userName");
		User loggedUser = userService.findByName(userName).get();
		Team team = teamService.findById(id).get();
		User user = userService.findById(userId).get();
		boolean isAdmin = team.isAdmin(loggedUser) || loggedUser.getRoles().contains("ADMIN");
		RedirectView rv;

		if (isAdmin) {
			if (accept) {
				if (team.getRequests().contains(userId) && user.getTeam() == null) {
					team.getRequests().remove(userId);
					user.setTeam(team);
					userService.saveUser(user);
					teamService.saveTeam(team);
					rv = new RedirectView("/teams/" + id);
				} else {
					team.getRequests().remove(userId);
					rv = new RedirectView("/teams/" + id + "?teamError=true");
				}
			} else {
				if (team.getRequests().contains(userId)) {
					team.getRequests().remove(userId);
					teamService.saveTeam(team);
					rv = new RedirectView("/teams/" + id);
				} else {
					team.getRequests().remove(userId);
					rv = new RedirectView("/teams/" + id + "?teamError=true");
				}
			}
		} else {
			rv = new RedirectView("/teams/" + id + "?teamError=true");
		}
		rv.setExposeModelAttributes(false);
		return rv;
	}

	@PostMapping("/teams/{id}/addGame")
	public View addGame(Model model, @PathVariable Long id, @RequestParam String name) {
		String userName = (String) model.getAttribute("userName");
		User loggedUser = userService.findByName(userName).get();
		Team team = teamService.findById(id).get();
		boolean isAdmin = team.isAdmin(loggedUser) || loggedUser.getRoles().contains("ADMIN");
		RedirectView rv;
		if (isAdmin) {
			Game game = gameService.findByName(name).get();
			team.getGames().add(game);
			teamService.saveTeam(team);
			rv = new RedirectView("/teams/" + id);
		} else {
			rv = new RedirectView("/teams/" + id + "?teamError=true");
		}
		rv.setExposeModelAttributes(false);
		return (rv);
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
