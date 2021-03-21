package com.victorious.team;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.victorious.tournament.Tournament;

@Entity
@Table(name = "teams")
public class Team {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(length = 1000, nullable = true)
	private String description;
	
	@ManyToMany
	private List<Tournament> tournaments;

	public Team() {}
	
	public Team(String name, String description) {
		this.name = name;
		this.description = description;
		this.tournaments = new ArrayList<Tournament>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Tournament> getTournaments() {
		return tournaments;
	}

	public void setTournaments(List<Tournament> tournaments) {
		this.tournaments = tournaments;
	}

	public void addTournament(Tournament tournament){
		this.tournaments.add(tournament);
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
}
