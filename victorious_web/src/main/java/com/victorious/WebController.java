package com.victorious;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
	
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
	
	@RequestMapping(value = {"/", "index"} )
	public String index(Model model, @RequestParam(required = false) boolean settingsError, @RequestParam(required = false) boolean settingsSuccess){
		model.addAttribute("settingsError", settingsError);
		model.addAttribute("settingsSuccess", settingsSuccess);
		return "index";
	}	

	@RequestMapping("/league")
	public String league(Model model){
		
		return "league";
	}
	
	@RequestMapping("/contact")
	public String contact(Model model){
		
		return "contact";
	}
	
	@RequestMapping("/newTournament")
	public String newTournament(Model model){
		
		return "newTournament";
	}
	
	@RequestMapping("/newTeam")
	public String newTeam(Model model){
		
		return "newTeam";
	}
	
}
