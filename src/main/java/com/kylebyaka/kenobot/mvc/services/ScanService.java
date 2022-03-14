package com.kylebyaka.kenobot.mvc.services;

import com.kylebyaka.kenobot.mvc.models.db.Film;
import com.kylebyaka.kenobot.mvc.models.db.Viewer;
import com.kylebyaka.kenobot.utils.BotUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.kylebyaka.kenobot.utils.BotUtil.sendLogMessage;

@Service
public class ScanService {

    private static final String EMOJI_REGEX = "(:\\w*:)";
    private static final UnicodeEmoji CHECKMARK_EMOJI = Emoji.fromUnicode("☑️");
    @Autowired
    private FilmService filmService;

    @Autowired
    private ViewerService viewerService;

    @Autowired
    private JDA jda;

    @Async
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void scheduledScan() {
        ThreadChannel kinci = BotUtil.findThreadChannel(jda, "кинцы");
        List<Message> allMessagesFromChannel = BotUtil.getAllMessagesFromChannel(kinci);
        List<Film> films = allMessagesFromChannel.stream()
                .map(this::buildFilm)
                .toList();
        List<Film> all = filmService.findAll();
        all.removeAll(films);
        
        filmService.deleteAll(all);
        filmService.addAll(films);
        sendLogMessage(jda, "Scheduled scan success!");
    }

    public void globalScan() {
//        if (!user.getName().equals("kylebyaka")) {
//            sendLogMessage(jda, "Scan is not permitted");
//            return;
//        }
        ThreadChannel kinci = BotUtil.findThreadChannel(jda, "кинцы");
        List<Message> allMessagesFromChannel = BotUtil.getAllMessagesFromChannel(kinci);
        saveAllViewers(allMessagesFromChannel);
        List<Film> films = allMessagesFromChannel.stream()
                .map(this::buildFilm)
                .toList();
        filmService.addAll(films);
        sendLogMessage(jda, "Scheduled scan success!");
    }

    private void saveAllViewers(List<Message> allMessagesFromChannel) {
        Set<Viewer> viewers = allMessagesFromChannel.stream()
                .map(message -> buildViewer(message.getAuthor()))
                .collect(Collectors.toSet());
        Set<Viewer> viewersFromReactions = allMessagesFromChannel.stream()
                .flatMap(message -> message.retrieveReactionUsers(CHECKMARK_EMOJI).complete().stream())
                .map(this::buildViewer)
                .collect(Collectors.toSet());
        viewers.addAll(viewersFromReactions);
        viewerService.addAll(viewers);
    }

    private Film buildFilm(Message ms) {
        Viewer viewer = buildViewer(ms.getAuthor());
        return Film.builder()
                .filmId(ms.getIdLong())
                .name(getName(ms))
                .votes(getVotes(ms, viewer))
                .viewed(ms.getContentDisplay().startsWith("~~"))
                .addedBy(viewer)
                .build();
    }

    @NotNull
    private String getName(Message ms) {
        String filmName = ms.getContentDisplay();
        String removeEmoji = filmName.replaceAll(EMOJI_REGEX, "");

        if (removeEmoji.startsWith("~~")) {
            return StringUtils.capitalize(removeEmoji.replace("~~", "").trim());
        } else {
            return StringUtils.capitalize(removeEmoji.trim());
        }
    }

    private List<Viewer> getVotes(Message ms, Viewer viewer) {
        return ms.retrieveReactionUsers(CHECKMARK_EMOJI)
                .complete()
                .stream()
                .filter(user -> !user.getName().equals(viewer.getName()))
                .map(this::getViewer)
                .toList();
    }

    @NotNull
    private Viewer getViewer(User user) {
        long idLong = user.getIdLong();
        return viewerService.findById(idLong).orElse(viewerService.add(buildViewer(user)));
    }

    private Viewer buildViewer(User user) {
        return Viewer.builder()
                .viewerId(user.getIdLong())
                .name(user.getName())
                .build();
    }
}
