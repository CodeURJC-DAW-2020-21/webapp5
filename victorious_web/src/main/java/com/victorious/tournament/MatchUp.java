package com.victorious.tournament;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.victorious.team.Team;

@Entity
public class MatchUp {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Team team1, team2;
    
    private int score1,score2;

    private boolean isPlayed;

    public MatchUp(){ }

    public MatchUp(Team team1, Team team2){
        this.team1=team1;
        this.team2=team2;
        this.isPlayed=false;
        this.score1=0;
        this.score2=0;
    }

    public Team getWinner(){

        if (score1>score2){
            return team1;
        }else{
            return team2;
        }
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean isPlayed) {
        this.isPlayed = isPlayed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}