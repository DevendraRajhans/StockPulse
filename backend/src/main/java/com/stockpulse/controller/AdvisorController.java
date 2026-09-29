package com.stockpulse.controller;

import com.stockpulse.advisor.CommerceAdvisorRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/advisor")
@CrossOrigin(origins = "http://localhost:5173")
public class AdvisorController {
    
    @Autowired
    private CommerceAdvisorRouter advisorRouter;
    
    @PutMapping("/strategy")
    public String setStrategy(@RequestParam String strategy) {
        try {
            CommerceAdvisorRouter.Strategy strat = CommerceAdvisorRouter.Strategy.valueOf(strategy.toUpperCase());
            advisorRouter.setStrategy(strat);
            return "Strategy set to: " + strat;
        } catch (IllegalArgumentException e) {
            return "Invalid strategy. Valid options: RULE, AI";
        }
    }
    
    @GetMapping("/strategy")
    public String getStrategy() {
        return "Current strategy: " + advisorRouter.getCurrentStrategy();
    }
}