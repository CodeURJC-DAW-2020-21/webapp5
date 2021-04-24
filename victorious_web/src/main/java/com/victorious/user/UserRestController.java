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
import com.victorious.game.Game;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	
	interface UserDetails extends User.Basic, User.UserTeam, Team.Basic, Game.Basic {}
	
	private static final String POSTS_FOLDER = "posts";

    @Autowired
    private UserService userService;
    
    @Autowired
	private ImageService imgService;

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
    public ResponseEntity<?> user (@PathVariable String name){
        Optional<User> oUser = userService.findByName(name);
        if(!oUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oUser);
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> updateUser (@RequestBody User user, @PathVariable String name){
        Optional<User> oUser = userService.findByName(name);
        if(!oUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        oUser.get().setName(user.getName());
        oUser.get().setEmail(user.getEmail());
        oUser.get().setEncodedPassword(user.getEncodedPassword());
        oUser.get().setRiot(user.getRiot());
        oUser.get().setBlizzard(user.getBlizzard());
        oUser.get().setPsn(user.getPsn());
        oUser.get().setXbox(user.getXbox());
        oUser.get().setSteam(user.getSteam());

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(oUser.get()));
    }

    @PostMapping("/")
    public ResponseEntity<User> newUser (@RequestBody User user){
        String name = user.getName();
        String email = user.getEmail();
        if(userService.findByName(name).isPresent() && userService.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }
    
    @GetMapping("/{name}/image")
	public ResponseEntity<Object> downloadUserImage(@PathVariable String name, HttpServletRequest request) throws MalformedURLException {
		
		Principal principal = request.getUserPrincipal();
		String userLoggedName = principal.getName();
		Optional<User> userLogged = userService.findByName(userLoggedName);
		Optional<User> user = userService.findByName(name);
		
		if (user.isPresent()) {
			if (userLogged.get().getId() == user.get().getId()) {
				return this.imgService.createResponseFromImage(POSTS_FOLDER, user.get().getId());
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
    
    @PostMapping("/{name}/image")
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