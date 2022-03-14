package com.kylebyaka.kenobot.utils;

import com.kylebyaka.kenobot.mvc.exceptions.db.DatabaseException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BotUtil {
    public static final String FIRST_WORD_FROM_STRING = "^([а-яА-Я0-9]+)";

    private BotUtil() {
    }

    public static void logMessage(@NotNull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {         //If this message was sent to a Guild TextChannel
            log(event, event.getChannel());
        } else if (event.isFromType(ChannelType.GUILD_PUBLIC_THREAD)) {
            log(event, event.getGuildChannel());
        }
    }

    private static void log(@NotNull MessageReceivedEvent event, MessageChannel messageChannel) {
        Guild guild = event.getGuild();             //The Guild that this message was sent in. (note, in the API, Guilds are Servers)

        String name = event.getAuthor().getName();
        Message message = event.getMessage();

        System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), messageChannel.getName(), name, message.getContentDisplay());
    }

    public static ThreadChannel findThreadChannel(JDA jda, String channelName) {
        return jda.getThreadChannels().stream()
                .filter(threadChannel -> threadChannel.getName().equals(channelName))
                .findFirst().orElseThrow(() -> new DatabaseException("Channel not found"));
    }

    public static TextChannel findTextChannel(JDA jda, String channelName) {
        return jda.getTextChannels().stream()
                .filter(threadChannel -> threadChannel.getName().equals(channelName))
                .findFirst().orElseThrow(() -> new DatabaseException("Channel not found"));
    }

    public static List<Message> getAllMessagesFromChannel(MessageChannel channel) {
        MessageHistory messageHistory = MessageHistory.getHistoryAfter(channel, "0").limit(100).complete();
        List<Message> messages = new ArrayList<>(messageHistory.getRetrievedHistory());
        while (!messageHistory.getRetrievedHistory().isEmpty()) {
            String lastId = getId(messageHistory);
            messageHistory = MessageHistory.getHistoryAfter(channel, lastId)
                    .limit(100)
                    .complete();
            messages.addAll(messageHistory.getRetrievedHistory());
        }
        return messages;
    }

    private static String getId(MessageHistory messageHistory) {
        return messageHistory.getRetrievedHistory()
                .stream()
                .min(getMessageComparator()).get().getId();
    }

    private static Comparator<Message> getMessageComparator() {
        return (o1, o2) -> o2.getTimeCreated().compareTo(o1.getTimeCreated());
    }

    public static void sendLogMessage(JDA jda, String text) {
        jda.getTextChannels().stream()
                .filter(textChannel -> textChannel.getName().equals("канал-кенобота"))
                .findFirst().get().sendMessage(text).queue();
    }
}
