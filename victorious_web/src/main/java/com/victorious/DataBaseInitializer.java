package com.victorious;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.victorious.team.Team;
import com.victorious.team.TeamRepository;
import com.victorious.tournament.Tournament;
import com.victorious.tournament.TournamentRepository;
import com.victorious.user.User;
import com.victorious.user.UserRepository;
import com.victorious.game.Game;
import com.victorious.game.GameRepository;

@Component
public class DataBaseInitializer {
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TournamentRepository tournamentRepository;
	
	@Autowired
	private GameRepository gameRepository;

	@PostConstruct
	public void init() {
		
		//Sample Games
		Game LeagueOfLegends = new Game("League of Legends", "LOL");
		gameRepository.save(LeagueOfLegends);

		//Sample Teams
		
		Team team1 = new Team("FNATIC", "Fnatic, occasionally stylized as fnatic and abbreviated as FNC, is a professional gaming organization with registered offices in the United Kingdom, Australia and the Netherlands. Fnatic is considered as a European team. Founded by Sam Mathews with Joris Van Laerhoven and Anne Mathews in 2004, and mainly operating out of their London offices, Fnatic has fielded numerous well known professional gamers in a variety of esports titles.");
		teamRepository.save(team1);

		Team team2 = new Team("Moscow5", "Moscow5, occasionally stylized as fnatic and abbreviated as M5, is a professional gaming organization with registered offices in the United Russia, Australia and the Netherlands. Fnatic is considered as a russian team. Founded by Putin with Joris Van Laerhoven and Anne Mathews in 2004, and mainly operating out of their URRSS offices, Moscow5 has fielded numerous well known professional gamers in a variety of esports titles.");
		teamRepository.save(team2);
		
		for(int i=0; i<8; i++){
			Team team = new Team("TEAM " + i, "Description of team " + i);
			teamRepository.save(team);
		}
		
		//Sample Users
		
		User user1 = new User("nUser", "nUser@gmail.com", "1111", "ROLE_USER");
        User user2 = new User("aUser", "aUser@gmail.com", "2222", "ROLE_USER", "ROLE_ADMIN");

        userRepository.save(user1);
        userRepository.save(user2);

		//Sample Tournamnets
		Tournament tournament1 = new Tournament("League of Legends Champioship", "League of Legends (LoL) is a multiplayer online battle arena video game developed and published by Riot Games. The goal is usually to destroy the opposing team's \"Nexus\", a structure that lies at the heart of a base protected by defensive structures.", 32, "12-13-20 at 13:00", "12-13-20 at 14:00", LeagueOfLegends);
		tournament1.addTeam(team1);
		tournament1.addTeam(team2);
		tournamentRepository.save(tournament1);
		
		
		Tournament tournament2 = new Tournament("League of Legends Seasonal", "League of Legends (LoL) is a multiplayer online battle arena video game developed and published by Riot Games. The goal is usually to destroy the opposing team's \"Nexus\", a structure that lies at the heart of a base protected by defensive structures.", 32, "12-09-20 at 13:00", "12-09-20 at 14:00", LeagueOfLegends);
		tournament2.addTeam(team1);
		tournamentRepository.save(tournament2);
		
	}

}
