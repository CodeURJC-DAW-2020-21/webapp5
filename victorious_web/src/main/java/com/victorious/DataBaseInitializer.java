package com.victorious;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.victorious.team.Team;
import com.victorious.team.TeamService;
import com.victorious.tournament.Tournament;
import com.victorious.tournament.TournamentService;
import com.victorious.user.User;
import com.victorious.user.UserService;
import com.victorious.game.Game;
import com.victorious.game.GameService;

@Service
public class DataBaseInitializer {
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private TournamentService tournamentService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() throws IOException, URISyntaxException{
		
		//Sample Games
		Game LeagueOfLegends = new Game("League of Legends");
		setGameImage(LeagueOfLegends,"/sample_images/lol.jpg");
		gameService.saveGame(LeagueOfLegends);
		
		Game CounterStrike = new Game("Counter Strike");
		setGameImage(CounterStrike,"/sample_images/cs.jpg");
		gameService.saveGame(CounterStrike);

		//Sample Tournamnets
		Tournament tournament1 = new Tournament("League of Legends Champioship", "League of Legends (LoL) is a multiplayer online battle arena video game developed and published by Riot Games. The goal is usually to destroy the opposing team's \"Nexus\", a structure that lies at the heart of a base protected by defensive structures.", 32, "12-13-20 at 13:00", "12-13-20 at 14:00", LeagueOfLegends);
		//tournament1.addTeam(team1);
		//tournament1.addTeam(team2);
		tournamentService.saveTournament(tournament1);
		
		for(int i=0; i<10; i++) {
			Tournament tournament = new Tournament("Tournament " + i, "Description of Tournament " + i, 32, "12-20-20 at 13:00", "12-20-20 at 14:00", LeagueOfLegends );
			//tournament.addTeam(team1);
			tournamentService.saveTournament(tournament);
		}
		
		//Sample Teams
		Team team1 = new Team("FNATIC", "Fnatic, occasionally stylized as fnatic and abbreviated as FNC, is a professional gaming organization with registered offices in the United Kingdom, Australia and the Netherlands. Fnatic is considered as a European team. Founded by Sam Mathews with Joris Van Laerhoven and Anne Mathews in 2004, and mainly operating out of their London offices, Fnatic has fielded numerous well known professional gamers in a variety of esports titles.");
		setTeamImage(team1,"/sample_images/fnatic.jpg");
		team1.addGame(LeagueOfLegends);
		team1.getTournaments().add(tournament1);
		teamService.saveTeam(team1);

		Team team2 = new Team("G2 ESPORTS", "G2esports, occasionally stylized and abreviated as G2,  formerly known as Gamers2, is a Spanish professional esports organization currently located in Berlin, Germany and founded in November 2013 by Carlos \"ocelote\" Rodriguez.");
		setTeamImage(team2,"/sample_images/G2.jpg");
		team2.addGame(LeagueOfLegends);
		team2.getTournaments().add(tournament1);
		teamService.saveTeam(team2);
		
		for(int i=0; i<8; i++){
			Team team = new Team("TEAM " + i, "Description of team " + i);
			setTeamImage(team,"/sample_images/team_default.jpg");
			teamService.saveTeam(team);
		}
		
		//Sample Users
		User user1 = new User("nUser", "nUser@gmail.com", passwordEncoder.encode("pass"), "USER");
        User user2 = new User("aUser", "aUser@gmail.com", passwordEncoder.encode("adminpass"), "USER", "ADMIN");
        
        setUserImage(user1,"/sample_images/user_default.jpg");
        setUserImage(user2,"/sample_images/user_default.jpg");

        userService.saveUser(user1);
        userService.saveUser(user2);
        
        ArrayList<User> users = new ArrayList<User>();
		for(int i=0; i<10; i++){
			User user = new User("User" + i, "user" + i + "@gmail.com", passwordEncoder.encode("user" + i + "pass"), "USER");
			users.add(user);
			setUserImage(user,"/sample_images/user_default.jpg");
			if(i%2 == 0){
				user.setTeam(team1);
			} else{
				user.setTeam(team2);
			}
			
			userService.saveUser(user);
		}
		
	}
	
	public void setUserImage(User user, String classpathResource) throws IOException {
		user.setImage(true);
		Resource image = (Resource) new ClassPathResource(classpathResource);
		user.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
	}
	
	public void setTeamImage(Team team, String classpathResource) throws IOException {
		team.setImage(true);
		Resource image = (Resource) new ClassPathResource(classpathResource);
		team.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
	}
	
	public void setGameImage(Game game, String classpathResource) throws IOException {
		game.setImage(true);
		Resource image = (Resource) new ClassPathResource(classpathResource);
		game.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
	}
	
}
