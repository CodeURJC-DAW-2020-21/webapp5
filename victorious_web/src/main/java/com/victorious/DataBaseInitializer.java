package com.victorious;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.victorious.team.Team;
import com.victorious.team.TeamRepository;
import com.victorious.user.User;
import com.victorious.user.UserRepository;

@Component
public class DataBaseInitializer {
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostConstruct
	public void init() {
		
		//Sample Teams
		
		Team team1 = new Team("FNATIC", "Fnatic, occasionally stylized as fnatic and abbreviated as FNC, is a professional gaming organization with registered offices in the United Kingdom, Australia and the Netherlands. Fnatic is considered as a European team. Founded by Sam Mathews with Joris Van Laerhoven and Anne Mathews in 2004, and mainly operating out of their London offices, Fnatic has fielded numerous well known professional gamers in a variety of esports titles.");
		teamRepository.save(team1);
		
		for(int i=0; i<8; i++){
			Team team = new Team("TEAM " + i, "Description of team " + i);
			teamRepository.save(team);
		}
		
		//Sample Users
		
		User user1 = new User("nUser", "nUser@gmail.com", "1111"/*, "ROLE_USER"*/);
        User user2 = new User("aUser", "aUser@gmail.com", "2222"/*, "ROLE_USER", "ROLE_ADMIN"*/);

        userRepository.save(user1);
        userRepository.save(user2);
	}

}
