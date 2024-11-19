package com.pprisam.backend.domain.ai.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextThemeLoader implements CommandLineRunner {

    private final TextThemeRepository textThemeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (textThemeRepository.count() == 0) {
            textThemeRepository.save(new TextThemeEntity("기본", "Write in a neutral and general tone."));
            textThemeRepository.save(new TextThemeEntity("공식적/격식", "Write in a formal and respectful tone suitable for official announcements or business communication."));
            textThemeRepository.save(new TextThemeEntity("캐주얼/친근", "Write in a casual and friendly tone, as if speaking to a close friend."));
            textThemeRepository.save(new TextThemeEntity("감성적/시적", "Write in an emotional and poetic tone, emphasizing feelings and sentiment."));
            textThemeRepository.save(new TextThemeEntity("유머러스/재미있는", "Write in a humorous and witty tone that makes the message fun and engaging."));
            textThemeRepository.save(new TextThemeEntity("스토리텔링/서사적", "Write in a storytelling style, presenting the content as a short narrative with a flowing structure."));

        }
    }
}
