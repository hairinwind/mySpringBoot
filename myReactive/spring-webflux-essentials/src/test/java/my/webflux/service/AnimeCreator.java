package my.webflux.service;

import my.webflux.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved() {
        return Anime.builder().name("test anime").build();
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1)
                .name("test anime")
                .build();
    }

    public static Anime createValidUpdatedAnime() {
        return Anime.builder()
                .id(1)
                .name("test anime 2")
                .build();
    }
}
