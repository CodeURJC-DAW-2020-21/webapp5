package com.victorious.tournament;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.victorious.team.Team;

@Entity
@Table(name = "rounds")
public class Rounds {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany
    private List<Team> participants;

    @OneToMany
    private List<Match> matches;

    public Rounds(List<Team> participants){
        this.participants=participants;
        this.matches=new ArrayList<>();
    }

    public void iniRound(){
        int i=0;
        while(i<participants.size()){
        Match match = new Match();
        match.setTeam1(participants.get(i));
        match.setTeam2(participants.get(i+1));
        matches.add(match);
        i=i+2;
        }
    }

    public void endRound(Rounds round){
        List<Team> winners= new ArrayList<Team>();
        int i=0;
        while(i<matches.size()){
            winners.add(matches.get(i).getWinner());
        }
        participants=winners;
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

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

}
