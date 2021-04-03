package com.victorious.team;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public interface TeamService{

	public List<Team> findAll();
	public Page<Team> findAll(Pageable pageable);
	public List<Team> findAll(Sort sort);
	public Optional<Team> findById(Long id);
	public Optional<Team> findByName(String name);
	public Team createTeam(Team team);
	public Team updateTeam(Team team);
	public Team saveTeam(Team team);
	//public void deleteById(Long id);

}
