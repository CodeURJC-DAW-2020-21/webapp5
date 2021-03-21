package com.victorious.tournament;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.victorious.game.Game;
import com.victorious.team.Team;

@Entity
@Table(name="tournaments")

public class Tournament {

//ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000, nullable = true)
    private String description;
    
    private int currentPlayers;
    
    private int maxPlayers;

    private String iniDate;

    private String endDate;

    @ManyToMany
    private List<Team> participants;

    @OneToMany
    private List<Rounds> rounds;

    @OneToOne
    private Game game;

    private int roundNumber = 0;
	
	private boolean started;
	
	private boolean finished;

//CONSTRUCTORS
    public Tournament(){ }

    public Tournament(String name, String description, int maxPlayers, String iniDate, String endDate, Game game){
        super();
        this.name=name;
        this.description=description;
        this.currentPlayers=0;
        this.maxPlayers=maxPlayers;
        this.iniDate=iniDate;
        this.endDate=endDate;
        this.participants= new ArrayList<Team>();
        //initialize rounds?
         this.game=game;
    }

//FUNCTIONS
    public void addTeam(Team participant){
        if ((!participants.contains(participant)) && ((participants.size()) < maxPlayers)) {
			participants.add(participant);
        }
        currentPlayers = currentPlayers +1;
    }

//GETTERS AND SETTERS
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

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getIniDate() {
        return iniDate;
    }

    public void setIniDate(String iniDate) {
        this.iniDate = iniDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Team> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Team> participants) {
        this.participants = participants;
    }

    public List<Rounds> getRounds() {
        return rounds;
    }

    public void setRounds(List<Rounds> rounds) {
        this.rounds = rounds;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
}
