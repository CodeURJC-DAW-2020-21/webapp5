package com.victorious;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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
		Game LeagueOfLegends = new Game("League of Legends", "LOL");
		gameService.saveGame(LeagueOfLegends);

		//Sample Teams
		
		Team team1 = new Team("FNATIC", "Fnatic, occasionally stylized as fnatic and abbreviated as FNC, is a professional gaming organization with registered offices in the United Kingdom, Australia and the Netherlands. Fnatic is considered as a European team. Founded by Sam Mathews with Joris Van Laerhoven and Anne Mathews in 2004, and mainly operating out of their London offices, Fnatic has fielded numerous well known professional gamers in a variety of esports titles.");
		teamService.saveTeam(team1);

		Team team2 = new Team("Moscow5", "Moscow5, occasionally stylized as fnatic and abbreviated as M5, is a professional gaming organization with registered offices in the United Russia, Australia and the Netherlands. Fnatic is considered as a russian team. Founded by Putin with Joris Van Laerhoven and Anne Mathews in 2004, and mainly operating out of their URRSS offices, Moscow5 has fielded numerous well known professional gamers in a variety of esports titles.");
		teamService.saveTeam(team2);
		
		for(int i=0; i<8; i++){
			Team team = new Team("TEAM " + i, "Description of team " + i);
			teamService.saveTeam(team);
		}
		
		//Sample Users
		
		User user1 = new User("nUser", "nUser@gmail.com", passwordEncoder.encode("pass"), "USER");
        User user2 = new User("aUser", "aUser@gmail.com", passwordEncoder.encode("adminpass"), "USER", "ADMIN");

        userService.saveUser(user1);
        userService.saveUser(user2);

		//Sample Tournamnets
		Tournament tournament1 = new Tournament("League of Legends Champioship", "League of Legends (LoL) is a multiplayer online battle arena video game developed and published by Riot Games. The goal is usually to destroy the opposing team's \"Nexus\", a structure that lies at the heart of a base protected by defensive structures.", 32, "12-13-20 at 13:00", "12-13-20 at 14:00", LeagueOfLegends);
		tournament1.addTeam(team1);
		tournament1.addTeam(team2);
		tournamentService.saveTournament(tournament1);
		
		for(int i=0; i<10; i++) {
			Tournament tournament = new Tournament("Tournament " + i, "Description of Tournament " + i, 32, "12-20-20 at 13:00", "12-20-20 at 14:00", LeagueOfLegends );
			tournament.addTeam(team1);
			tournamentService.saveTournament(tournament);
		}
		
	}
	
}
