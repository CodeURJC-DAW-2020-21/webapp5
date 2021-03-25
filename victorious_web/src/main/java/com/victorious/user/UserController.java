package com.victorious.user;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	
	@GetMapping("/user/{name}/image")
	public ResponseEntity<Object> downloadImage(@PathVariable String name) throws SQLException {

		Optional<User> user = userService.findByName(name);
		if (user.isPresent() && user.get().getImageFile() != null) {

			Resource file = new InputStreamResource(user.get().getImageFile().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(user.get().getImageFile().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
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
	public View signUp(Model model, @RequestParam String name, @RequestParam String email, @RequestParam String password) throws IOException {
		RedirectView rv;
		if (!userService.findByName(name).isPresent()) {
			User user = new User(name, email, passwordEncoder.encode(password), "USER");
			setUserImage(user, "/sample_images/user_default.jpg");
			userService.saveUser(user);
			rv = new RedirectView("index");
		} else {
			rv = new RedirectView("/sign-up?signUpError=true");
		}
		return rv;
	}
	
	private void setUserImage(User user, String classPathResource) throws IOException {
		user.setImage(true);
		Resource image = new ClassPathResource(classPathResource);
		user.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
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
	public View settings(Model model, @PathVariable String name, String username, String email, String password, 
			MultipartFile imageField, String riot, String blizzard, String psn, String xbox, String steam, 
			HttpServletRequest request) throws IOException, SQLException, ServletException{
		
		RedirectView rv;
		Optional<User> user = userService.findByName(name);
		
		if ((!userService.findByName(username).isPresent()) && (!userService.findByEmail(email).isPresent())) {
			if (!username.isEmpty()) {
	            user.get().setName(username);
	        }
			
	        if (!email.isEmpty()) {
	            user.get().setEmail(email);
	        }
	        
	        if (!password.isEmpty()) {
	            user.get().setEncodedPassword(password);
	        }
	        
	        if(!imageField.isEmpty()) {
	        	updateImage(user.get(), imageField);
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
	        
	        userService.updateUser(user.get());
		    model.addAttribute("user", user.get());
		    request.logout();
		    rv = new RedirectView("/index?settingsSuccess=true");
		} else {
			rv = new RedirectView("/index?settingsSuccess=false");
		}
	    
		return rv;
	}

	private void updateImage(User user, MultipartFile imageField) throws IOException, SQLException {
		
		user.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
		user.setImage(true);
	
		// Maintain the same image loading it before updating the book
		User dbUser = userService.findById(user.getId()).orElseThrow();
		if (dbUser.hasImage()) {
			user.setImageFile(BlobProxy.generateProxy(dbUser.getImageFile().getBinaryStream(),
					dbUser.getImageFile().length()));
			user.setImage(true);
		}
	}
}
