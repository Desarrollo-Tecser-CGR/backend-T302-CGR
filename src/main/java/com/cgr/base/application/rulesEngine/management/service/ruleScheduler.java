package com.cgr.base.application.rulesEngine.management.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ruleScheduler {

    @Autowired
    private initDependencies ApplyRules;

    @Async
    @Scheduled(cron = "0 0 0 1 * ?") // Se ejecuta el primer día de cada mes a medianoche
    public void scheduleRulesExecution() {
        // 1. Ejecutar primero initializeDependencies()
        ApplyRules.initializeDependencies();

        try {
            Thread.sleep(20 * 60000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String[] generalRules = {"1", "2", "3", "4", "5", "6", "7", "8", "9A", "9B", "10", "11", "12", "13A", "13B", "14A", "14B", "15", "16A", "16B"};
        executeRulesWithDelay(generalRules, true);


        String[] specificRules = {"22A", "22B", "22C", "22D", "22E", "24", "25A", "25B", "GF", "26", "27", "28", "29A", "29B", "29C", "30", "31"};
        executeRulesWithDelay(specificRules, false);
    }

    private void executeRulesWithDelay(String[] rules, boolean isGeneral) {
        int delay = 0;
        for (String rule : rules) {
            int finalDelay = delay;
            new Thread(() -> {
                try {
                    Thread.sleep(finalDelay * 60000L);
                    if (isGeneral) {
                        ApplyRules.transferGeneralRules(rule);
                    } else {
                        ApplyRules.transferSpecificRules(rule);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
            delay += 30;
        }
    }

    
    
}
