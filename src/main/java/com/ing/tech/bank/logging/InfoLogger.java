package com.ing.tech.bank.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InfoLogger {
    public void log(String message) {
        log.info(message);
    }
}
