package com.victorious.user;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;


import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.victorious.team.Team;
import com.victorious.ImageService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	
	interface UserDetails extends User.Basic, User.UserTeam, Team.Basic  {}
	
	private static final String POSTS_FOLDER = "posts";

    @Autowired
    private UserService userService;
    
    @Autowired
	private ImageService imgService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/logged")
    @JsonView(UserDetails.class)
    public ResponseEntity<User> logged(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if(principal != null) {
            return ResponseEntity.ok(userService.findByName(principal.getName()).orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{name}")
    @JsonView(UserDetails.class)
    public ResponseEntity<User> user (@PathVariable String name){
        Optional<User> oUser = userService.findByName(name);
        if(!oUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oUser.get());
    }

    @PutMapping("/{name}")
    @JsonView(UserDetails.class)
    public ResponseEntity<User> updateUser (@RequestBody User user, @PathVariable String name){
        Optional<User> oUser = userService.findByName(name);
        if(!oUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if(!user.getName().isEmpty()){
        	oUser.get().setName(user.getName());
        }
        if(!user.getEmail().isEmpty()){
        	oUser.get().setEmail(user.getEmail());
        }
        if(!user.getEncodedPassword().isEmpty()){
        	oUser.get().setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
        }
        if(!user.getRiot().isEmpty()){
        	oUser.get().setRiot(user.getRiot());
        }
        if(!user.getBlizzard().isEmpty()){
        	oUser.get().setBlizzard(user.getBlizzard());
        }
        if(!user.getPsn().isEmpty()){
        	oUser.get().setPsn(user.getPsn());
        }
        if(!user.getXbox().isEmpty()){
        	oUser.get().setXbox(user.getXbox());
        }
        if(!user.getSteam().isEmpty()){
        	oUser.get().setSteam(user.getSteam());
        }
        
        userService.updateUser(oUser.get());
        
        return ResponseEntity.status(HttpStatus.OK).body(oUser.get());
    }

    @PostMapping("/")
    @JsonView(UserDetails.class)
    public ResponseEntity<User> newUser (@RequestBody User user){
    	
        if(userService.findByName(user.getName()).isPresent() && userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        User newUser = new User(user.getName(),user.getEmail(),passwordEncoder.encode(user.getEncodedPassword()), "USER");
        userService.saveUser(newUser);
        
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
		return ResponseEntity.created(location).body(newUser);
    }
    
    @GetMapping("/{name}/image")
    @JsonView(UserDetails.class)
	public ResponseEntity<Object> downloadUserImage(@PathVariable String name, HttpServletRequest request) throws MalformedURLException {

		Optional<User> user = userService.findByName(name);
		
		if (user.isPresent()) {
			return this.imgService.createResponseFromImage(POSTS_FOLDER, user.get().getId());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
    
    @PostMapping("/{name}/image")
    @JsonView(UserDetails.class)
	public ResponseEntity<Object> uploadUserImage(@PathVariable String name, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException {

    	Principal principal = request.getUserPrincipal();
		String userLoggedName = principal.getName();
		Optional<User> userLogged = userService.findByName(userLoggedName);
		Optional<User> user = userService.findByName(name);
		
		if (user.isPresent()) {
			if (userLogged.get().getId() == user.get().getId()) {
				URI location = fromCurrentRequest().build().toUri();
				
				user.get().setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				userService.saveUser(user.get());
				imgService.saveImage(POSTS_FOLDER, user.get().getId(), imageFile);
				
				return ResponseEntity.created(location).build();
				
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}