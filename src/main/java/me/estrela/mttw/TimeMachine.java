package me.estrela.mttw;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TimeMachine {

    public Zoned utc() {
        return new Zoned(ZoneOffset.UTC);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class Zoned {

        private final LocalDateTime now;

        public Zoned(ZoneOffset zoneOffset) {
            this.now = LocalDateTime.now(zoneOffset);
        }

        public LocalDateTime now() {
            return now;
        }

        public LocalDateTime nowMinusSeconds(long seconds) {
            return now.minusSeconds(seconds);
        }

    }

}
