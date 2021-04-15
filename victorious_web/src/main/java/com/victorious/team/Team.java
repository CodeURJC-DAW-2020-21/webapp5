package com.victorious.team;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.victorious.user.User;
import com.fasterxml.jackson.annotation.JsonView;
import com.victorious.game.Game;
import com.victorious.tournament.Tournament;

@Entity
public class Team {
	public interface Basico{}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Basico.class)
	private Long id;
	
	@Column(nullable = false)
	@JsonView(Basico.class)
	private String name;
	
	@JsonView(Basico.class)
	@Column(length = 1000, nullable = true)
	private String description;

	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Tournament> tournaments;
	
	//@JsonView(Basico.class)
	@ManyToMany
	private List<Game> games;
	
	@OneToOne
	//@JsonView(Basico.class)
	private User creator;
	
	@OneToMany(mappedBy="team")
//	@JsonView(Basico.class)
	private List<User> users;
	
	@ManyToMany
//	@JsonView(Basico.class)
	private List<User> admins;
	
	@ElementCollection
	@CollectionTable(name ="requests")
//	@JsonView(Basico.class)
	private List<Long> requests;
	
	@Lob
//	@JsonView(Basico.class)
	private Blob imageFile;
	
	private boolean image;
	
	@Column
//	@JsonView(Basico.class)
	private int nVictories;

	@Column
//	@JsonView(Basico.class)
	private int nLoses;
	
	@Column
//	@JsonView(Basico.class)
	private String recordV;
	
	@Column
//	@JsonView(Basico.class)
	private String recordL;

	public Team() {}
	
	public Team(String name, String description) {
		this.name = name;
		this.description = description;
		this.tournaments = new ArrayList<Tournament>();
		this.initLists();
		this.nVictories = 0;
		this.nLoses = 0;
		this.recordV = "";
		this.recordL = "";
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
	
	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}
	
	public void addGame(Game game){
		this.games.add(game);
	}
	
	public void deleteGame(Game game){
		this.games.remove(game);
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public List<User> getAdmins() {
		return admins;
	}

	public void setAdmins(List<User> admins) {																																																																												
		this.admins = admins;
	}
	
	public void addAdmin(User user){
		this.admins.add(user);
	}
	
	public boolean isAdmin(User user){
		return admins.contains(user);
	}
	
	public List<Long> getRequests() {
		return requests;
	}

	public void setRequests(List<Long> requests) {
		this.requests = requests;
	}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public Blob getImageFile() {
		return imageFile;
	}

	public void setImageFile(Blob image) {
		this.imageFile = image;
	}
	
	public boolean hasImage(){
		return this.image;
	}

	public void setImage(boolean image){
		this.image = image;
	}
	
	public void initLists(){
		users = new ArrayList<>();
		games = new ArrayList<>();
		admins = new ArrayList<>();
		requests = new ArrayList<>();
	}
	

	public int getnVictories() {
		return nVictories;
	}

	public void setnVictories(int nVictories) {
		this.nVictories = nVictories;
	}

	public int getnLoses() {
		return nLoses;
	}

	public void setnLoses(int nLoses) {
		this.nLoses = nLoses;
	}

	public String getRecordV() {
		return recordV;
	}

	public void setRecordV(String recordV) {
		this.recordV = recordV;
	}

	public String getRecordL() {
		return recordL;
	}

	public void setRecordL(String recordL) {
		this.recordL = recordL;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
}
