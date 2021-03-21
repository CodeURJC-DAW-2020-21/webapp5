package com.victorious.tournament;

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
    }



}
