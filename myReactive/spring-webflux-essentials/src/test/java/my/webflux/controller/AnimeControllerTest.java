package my.webflux.controller;

import jakarta.validation.Valid;
import my.webflux.domain.Anime;
import my.webflux.repository.AnimeRepository;
import my.webflux.service.AnimeCreator;
import my.webflux.service.AnimeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeEach
    public void setUp() {
        BDDMockito.when(animeServiceMock.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeServiceMock.findById(1))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeServiceMock.save(any(Anime.class)))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeServiceMock.delete(1))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeServiceMock.update(any(Anime.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("list all returns a flux of Anime")
    public void listAllReturns() {
        StepVerifier.create(animeController.listAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns anime")
    public void findByIdReturns() {
        StepVerifier.create(animeController.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("save creates anime")
    public void saveCreatesAnime() {
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeController.save(animeToSave))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("batch save")
    public void batchSaveAnimes() {
        BDDMockito.when(animeServiceMock.saveAll(List.of(AnimeCreator.createAnimeToBeSaved(), AnimeCreator.createAnimeToBeSaved())))
                .thenReturn(Flux.just(anime, anime));
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeController.batchSave(List.of(animeToSave, animeToSave)))
                .expectSubscription()
                .expectNext(anime, anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete removes anime")
    public void deleteAnime() {
        StepVerifier.create(animeController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update successfully")
    public void updateAnime() {
        Anime animeToUpdate = AnimeCreator.createValidUpdatedAnime();
        StepVerifier.create(animeController.update(animeToUpdate))
                .expectSubscription()
                .verifyComplete();
    }

}