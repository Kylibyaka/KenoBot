package com.kylebyaka.kenobot;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Main.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
