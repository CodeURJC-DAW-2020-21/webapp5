package com.victorious;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class WebController {
	
	
	@RequestMapping(value = {"/", "index"} )
	public String index(Model model){
		
		return "index";
	}
	
	@RequestMapping("/tournaments")
	public String tournaments(){
		
		return "tournaments";
	}	
	
	@RequestMapping("/tournaments/tournament")
	public String tournament(Model model){
		
		return "tournament";
	}	

	@RequestMapping("/league")
	public String league(Model model){
		
		return "league";
	}
	
	@RequestMapping("/contact")
	public String contact(Model model){
		
		return "contact";
	}
	
	@RequestMapping("/login")
	public String login(Model model){
		
		return "login";
	}
	
	@RequestMapping("/settings")
	public String settings(Model model){
		
		return "settings";
	}
	
	@RequestMapping("/sign-up")
	public String signUp(Model model){
		
		return "sign-up";
	}
	
	@RequestMapping("/user")
	public String user(Model model){
		
		return "user";
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
