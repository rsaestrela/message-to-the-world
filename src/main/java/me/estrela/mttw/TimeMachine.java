package me.estrela.mttw;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TimeMachine {

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

}
