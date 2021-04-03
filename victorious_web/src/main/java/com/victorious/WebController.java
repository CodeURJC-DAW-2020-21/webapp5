package com.victorious;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
	
	@Autowired
	private JavaMailSender mailSender;
	
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
	
	@GetMapping("/contact")
	public String showContactForm(Model model){
		return "contact";
	}
	
	@PostMapping("/contact")
	public String submitMessage(HttpServletRequest  request,
			@RequestParam String name,
			@RequestParam String email,
			@RequestParam String subject,
			@RequestParam String content) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("SpringMail@gmail.com");
		message.setTo("VictoriusWeb@gmail.com");
		String mailSubject = name +"has sent a message";
		String mailContent = "Sender Name: " + name + "\n"
							+"Sender Email: " + email + "\n"
							+"Message Subject: " + subject +"\n"
							+ "--------------" + "\n"
							+ content;
		message.setSubject(mailSubject);
		message.setText(mailContent);
		
		mailSender.send(message);
		return  "contact";
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
