package it.univaq.sose.simplebankingsoapservice.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogColorLevelConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        Level level = event.getLevel();
        switch (level.toInt()) {
            case Level.ERROR_INT: return "\u001b[31m" + level; // Red
            case Level.WARN_INT:  return "\u001b[33m" + level; // Yellow
            case Level.INFO_INT:  return "\u001b[32m" + level; // Green
            case Level.DEBUG_INT: return "\u001b[36m" + level; // Cyan
            default: return "\u001b[0m" + level; // Normal color
        }
    }
}