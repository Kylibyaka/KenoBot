package com.kylebyaka.kenobot.mvc.exceptions;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionHandler {

    @AfterThrowing(pointcut = "execution(* com.kylebyaka.kenobot.listeners.CommandsListener.onMessageReceived(..))", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) {
        log.error(ex.getMessage());
        MessageReceivedEvent event = (MessageReceivedEvent) joinPoint.getArgs()[0];
        event.getChannel().sendMessage("Что-то пошло не так!").queue();
    }

    @AfterThrowing(pointcut = "execution(* com.kylebyaka.kenobot.listeners.CommandsListener.onMessageReceived(..))", throwing = "ex")
    public void handleValidationException(JoinPoint joinPoint, ValidationException ex) {
        log.error(ex.getMessage());
        MessageReceivedEvent event = (MessageReceivedEvent) joinPoint.getArgs()[0];
        event.getChannel().sendMessage("Что-то пошло не так! " + ex.getMessage()).queue();
    }
}
