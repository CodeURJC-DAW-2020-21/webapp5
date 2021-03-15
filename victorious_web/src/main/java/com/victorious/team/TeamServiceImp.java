package com.victorious.team;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImp implements TeamService{

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public List<Team> findAll() {
		return teamRepository.findAll();
	}

	@Override
	public Page<Team> findAll(Pageable pageable) {
		return teamRepository.findAll(pageable);
	}

	@Override
	public Optional<Team> findById(Long id) {
		return teamRepository.findById(id);
	}

	@Override
	public Optional<Team> findByName(String name) {
		return teamRepository.findByName(name);
	}

	@Override
	public Team createTeam(Team team) {
		return teamRepository.save(team);
	}

	@Override
	public Team updateTeam(Team team) {
		return teamRepository.save(team);
	}
	
	/*@Override
	public void deleteById(Long id) {
		teamRepository.deleteById(id);	
	}*/

}
