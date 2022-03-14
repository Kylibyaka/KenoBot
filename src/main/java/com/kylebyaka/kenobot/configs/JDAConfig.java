package com.kylebyaka.kenobot.configs;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class JDAConfig {

    @Value("${com.kylebyaka.kenobot.token}")
    private String token;

    @Bean
    public JDA jda(Map<String, ListenerAdapter> listeners) throws InterruptedException {
        JDA jda = JDABuilder.createDefault(token) // The token of the account that is logging in.
                .addEventListeners(listeners.values().toArray())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)// An instance of a class that will handle events.
                .build();
        jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
        System.out.println("Finished Building JDA!");
        return jda;
    }
}