package com.victorious.tournament;

import java.util.List;
import java.util.Optional;

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
    
}
