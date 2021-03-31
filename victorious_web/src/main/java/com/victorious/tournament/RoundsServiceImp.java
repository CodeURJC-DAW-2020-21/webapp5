package com.victorious.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.victorious.team.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoundsServiceImp implements RoundsService{

    @Autowired
    private RoundsRepository roundRepository;
    
    @Override
    public List<Rounds> findAll() {
        return roundRepository.findAll();
    }

    @Override
    public Optional<Rounds> findById(Long id) {
        return roundRepository.findById(id);
    }
    
    public Rounds saveRounds(Rounds round){
        
        return roundRepository.save(round);
    }

    public List<Team> getWinners(Rounds round){
        List<Team> winners= new ArrayList<Team>();
        int i=0;
        while(i<round.getMatches().size()){
            winners.add(round.getMatches().get(i).getWinner());
        }
        return winners;
    }
}
