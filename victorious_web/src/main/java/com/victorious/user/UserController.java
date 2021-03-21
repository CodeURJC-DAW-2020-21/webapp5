package com.victorious.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserComponent userComponent;
	
	@RequestMapping("/user/{name}")
	public String showUser(Model model, @PathVariable String name) {
			
		Optional<User> user = userService.findByName(name);

		if(user.isPresent()) {
			model.addAttribute("actualUser", user.get());
		}

		return "user";
	}

	@GetMapping("/login")
	public String loginError(Model model, @RequestParam(required = false) boolean loginError){
		model.addAttribute("loginError", loginError);
		return "login";
	}
	
	@RequestMapping("/login")
	public String login(Model model){
		
		return "login";
	}
	
	@GetMapping("/sign-up")
	public String signUpError(Model model, @RequestParam(required = false) boolean signUpError){
		model.addAttribute("signUpError", signUpError);
		return "sign-up";
	}
	
	@PostMapping("/sign-up")
	public View signUp(Model model, @RequestParam String name, @RequestParam String email, @RequestParam String password) {
		RedirectView rv;
		if (!userService.findByName(name).isPresent()) {
			User user = new User(name, email, password, "ROLE_USER");
			userService.saveUser(user);
			rv = new RedirectView("index");
		} else {
			rv = new RedirectView("/sign-up?signUpError=true");
		}
		return rv;
	}
	
	@GetMapping("/settings")
	public String settingsError(Model model, @RequestParam(required = false) boolean settingsError){
		model.addAttribute("settingsError", settingsError);
		return "settings";
	}
	
	@PostMapping("/settings")
	public String settings(Model model, @RequestParam String name, @RequestParam String email,
            @RequestParam String password, /*@RequestParam("pic") MultipartFile file,*/ @RequestParam String riot, 
            @RequestParam String blizzard, @RequestParam String psn, @RequestParam String xbox, @RequestParam String steam){
		
		User user = userComponent.getLoggedUser();

		if ((!userService.findByName(user.getName()).isPresent()) && (!userService.findByEmail(user.getEmail()).isPresent())) {
			if (!name.isEmpty()) {
	            System.out.println("Nombre " + name);
	            user.setName(name);
	        }
			
	        if (!email.isEmpty()) {
	            user.setEmail(email);
	        }
	        
	        if (!password.isEmpty()) {
	            user.setPassword(password);
	        }
	        
	        if (!riot.isEmpty()) {
	            user.setRiot(riot);
	        }
	        
	        if (!blizzard.isEmpty()) {
	            user.setBlizzard(blizzard);
	        }
	        
	        if (!psn.isEmpty()) {
	            user.setPsn(psn);
	        }
	        
	        if (!xbox.isEmpty()) {
	            user.setXbox(xbox);
	        }
	        
	        if (!steam.isEmpty()) {
	            user.setSteam(steam);
	        }
		}
		
        userService.saveUser(user);
		return "settings";
	}
}
