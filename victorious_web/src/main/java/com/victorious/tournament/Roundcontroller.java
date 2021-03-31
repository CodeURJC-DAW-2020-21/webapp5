package com.victorious.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

@Controller
public class Roundcontroller {
    
    @Autowired
    RoundsService roundService;

    @PostMapping("/tournament/{id}/{round}")
    public View updateRounds(Model model, @RequestParam String score1, @RequestParam String score2){
        
        
        return null;
    }

    @GetMapping("/tournament")
    public String showRounds(){
        
        return null;
    }
}
