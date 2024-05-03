package com.leapwise.banking.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MonthlyTurnoverService {

    private final AccountService accountService;

    @Scheduled(cron = "0 0 0 1 * *", zone = "Europe/Zagreb")
    @Profile("scheduled")
    public void calculateMonthlyTurnover() {
        this.accountService.calculateMonthlyTurnover();
    }

}
