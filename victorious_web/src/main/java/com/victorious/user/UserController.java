package com.victorious.user;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
	
	@RequestMapping("/user/{name}")
	public String showUser(Model model, @PathVariable String name) {
			
		Optional<User> user = userService.findByName(name);

		if(user.isPresent()) {
			model.addAttribute("user", user.get());
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
			User user = new User(name, email, passwordEncoder.encode(password), "USER");
			userService.saveUser(user);
			rv = new RedirectView("index");
		} else {
			rv = new RedirectView("/sign-up?signUpError=true");
		}
		return rv;
	}
	
	@GetMapping("/user/{name}/settings")
	public String settingsError(Model model, @PathVariable String name, @RequestParam(required = false) boolean settingsError){
		Optional<User> user = userService.findByName(name);

		if(user.isPresent()) {
			model.addAttribute("user", user.get());
		}
		return "settings";
	}
	
	@PostMapping("/user/{name}/settings")
	public View settings(Model model, @PathVariable String name, @RequestParam String username, @RequestParam String email,
            @RequestParam String password, /*@RequestParam("pic") MultipartFile file,*/ @RequestParam String riot, 
            @RequestParam String blizzard, @RequestParam String psn, @RequestParam String xbox, @RequestParam String steam, HttpServletRequest request) throws ServletException{
		RedirectView rv;
		Optional<User> user = userService.findByName(name);
		
		if(user.isPresent()) {
			if (!userService.findByName(username).isPresent()) {
				if (!username.isEmpty()) {
		            user.get().setName(username);
		        }
				
		        if (!email.isEmpty()) {
		            user.get().setEmail(email);
		        }
		        
		        if (!password.isEmpty()) {
		            user.get().setEncodedPassword(password);
		        }
		        
		        if (!riot.isEmpty()) {
		            user.get().setRiot(riot);
		        }
		        
		        if (!blizzard.isEmpty()) {
		            user.get().setBlizzard(blizzard);
		        }
		        
		        if (!psn.isEmpty()) {
		            user.get().setPsn(psn);
		        }
		        
		        if (!xbox.isEmpty()) {
		            user.get().setXbox(xbox);
		        }
		        
		        if (!steam.isEmpty()) {
		            user.get().setSteam(steam);
		        }
			}
		    userService.updateUser(user.get());
		    model.addAttribute("user", user.get());
		    request.logout();
		    rv = new RedirectView("/index?settingsSuccess=true");
		} else {
			rv = new RedirectView("/index?settingsError=true");
		}
		return rv;
	}
}
