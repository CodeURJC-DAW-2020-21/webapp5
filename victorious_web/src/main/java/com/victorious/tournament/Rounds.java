package com.victorious.tournament;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;
import com.victorious.team.Team;

@Entity
public class Rounds {
    
	public interface Basic{}
	interface RoundTeams{}
	interface RoundMatchUps{}
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private Long id;
    
    @ManyToMany
    @JsonView(RoundTeams.class)
    private List<Team> participants;

    @ManyToMany
    @JsonView(RoundTeams.class)
    List<Team> winners;

    @ManyToOne
    @JsonView(RoundTeams.class)
    private Team oddTeam;

    @OneToMany
    @JsonView(RoundMatchUps.class)
    private List<MatchUp> matches;
    
    @JsonView(Basic.class)
    private int numRound;

    public Rounds(){ }

    public Rounds(List<Team> participants){
        this.participants=participants;
        this.matches=new ArrayList<MatchUp>();
        this.numRound=0;
        this.winners=new ArrayList<Team>();;
    }

    public List<Team> getWinners(){
        int i=0;
        while(i<this.matches.size()){
            this.winners.add(matches.get(i).getWinner());
            i++;
        }
        return winners;
    }

    //FUNCTIONS
    public void addMatch(MatchUp match){
        this.matches.add(match);
    }

    public boolean isEvenRound(){
        return this.participants.size()%2==0;
    }

    public void setOddRound(){
        this.winners.add(this.participants.get(0));
        this.oddTeam=this.participants.get(0);
        this.participants.remove(0);
    }

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Team> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Team> participants) {
        this.participants = participants;
    }

    public List<MatchUp> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchUp> matches) {
        this.matches = matches;
    }

    public int getNumRound() {
        return numRound;
    }

    public void setNumRound(int numRound) {
        this.numRound = numRound;
    }

    public void setWinners(List<Team> winners) {
        this.winners = winners;
    }
}
