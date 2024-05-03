package my.webflux.service;

import my.webflux.domain.Anime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import my.webflux.repository.AnimeRepository;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void setupBeforeAll() {
        BlockHound.install();
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepositoryMock.delete(any(Anime.class)))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createValidUpdatedAnime()))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeRepositoryMock.saveAll(anyIterable()))
                .thenReturn(Flux.just(anime, anime));
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);
            task.get(100, TimeUnit.MILLISECONDS);
            fail("should fail");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    @DisplayName("findAll returns a flux of anime")
    public void findAllReturnFluxOfAnime() {
        StepVerifier.create(animeService.findAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns a Mono anime")
    public void findByIdReturnMonoAnime() {
        StepVerifier.create(animeService.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns Mono error")
    public void findByIdReturnError() {
        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(animeService.findById(2))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("save creates anime")
    public void saveCreatesAnime() {
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeService.save(animeToSave))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("save batch")
    public void saveBatchAnimes() {
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeService.saveAll(List.of(animeToSave, animeToSave)))
                .expectSubscription()
                .expectNext(anime, anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("save batch with empty name")
    public void saveBatchAnimesError() {
        BDDMockito.when(animeRepositoryMock.saveAll(anyIterable()))
                .thenReturn(Flux.just(anime, anime.withName("")));
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeService.saveAll(List.of(animeToSave, animeToSave.withName(""))))
                .expectSubscription()
                // .expectNext(anime)  // this only happens when validation happens after saveAll
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete removes anime")
    public void deleteAnime() {
        StepVerifier.create(animeService.delete(1))
                .expectSubscription()
                .verifyComplete();

    }

    @Test
    @DisplayName("delete returns Mono error")
    public void deleteAnimeError() {
        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(animeService.delete(2))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("update successfully")
    public void updateAnime() {
        Anime animeToUpdate = AnimeCreator.createValidUpdatedAnime();
        StepVerifier.create(animeService.update(animeToUpdate))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update returns error")
    public void updateAnimeError() {
        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Mono.empty());
        Anime animeToUpdate = AnimeCreator.createValidUpdatedAnime();
        StepVerifier.create(animeService.update(animeToUpdate))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}