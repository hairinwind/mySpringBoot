package my.webflux.integration;

import my.webflux.domain.Anime;
import my.webflux.repository.AnimeRepository;
import my.webflux.service.AnimeCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)

//@WebFluxTest
//@Import({AnimeService.class, AppConfiguration.class})
// the above two annotations can be replaced by these two
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AnimeControllerIT {

    @MockBean
    private AnimeRepository animeRepositoryMock;
    @Autowired
    private WebTestClient testClient;

    private Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(Flux.just(anime));
        BDDMockito.when(animeRepositoryMock.findById(1))
                .thenReturn(Mono.just(anime));
        BDDMockito.when(animeRepositoryMock.findById(2))
                .thenReturn(Mono.empty());
        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(Mono.just(anime));
        BDDMockito.when(animeRepositoryMock.delete(any(Anime.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createValidUpdatedAnime()))
                .thenReturn(Mono.empty());
        BDDMockito.when(animeRepositoryMock.saveAll(List.of(AnimeCreator.createAnimeToBeSaved(), AnimeCreator.createAnimeToBeSaved())))
                .thenReturn(Flux.just(anime, anime));
    }

    @Test
    @DisplayName("listAll returns a flux of Anime")
    public void listAll() {
        testClient.get()
                .uri("/animes")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(anime.getId())
                .jsonPath("$.[0].name").isEqualTo(anime.getName());

        //another way to verify Body Json
        testClient.get()
                .uri("/animes")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Anime.class)
                .hasSize(1)
                .contains(anime);
    }

    @Test
    @DisplayName("findById returns anime")
    public void findByIdReturns() {
        testClient.get()
                .uri("/animes/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Anime.class)
                .isEqualTo(anime);
    }

    @Test
    @DisplayName("findById error")
    public void findByIdError() {
        testClient.get()
                .uri("/animes/{id}", 2)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    @DisplayName("save creates anime")
    public void saveCreatesAnime() {
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();
        testClient.post()
                .uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToSave))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Anime.class)
                .isEqualTo(anime);
    }

    @Test
    @DisplayName("save empty anime name returns error")
    public void saveEmptyAnimeNameReturnError() {
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved().withName("");
        testClient.post()
                .uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToSave))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DisplayName("save batch anime")
    public void saveBatchAnime() {
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();
        testClient.post()
                .uri("/animes/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(List.of(animeToSave, animeToSave)))
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Anime.class)
                .isEqualTo(List.of(anime, anime));
    }

    @Test
    @DisplayName("save batch anime error")
    public void saveBatchAnimeError() {
        Anime animeToSave = AnimeCreator.createAnimeToBeSaved();
        testClient.post()
                .uri("/animes/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(List.of(animeToSave, animeToSave.withName(""))))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DisplayName("delete removes anime")
    public void deleteAnime() {
        testClient.delete()
                .uri("/animes/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("delete returns Mono error")
    public void deleteAnimeError() {
        testClient.delete()
                .uri("/animes/{id}", 2)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    @DisplayName("update successfully")
    public void updateAnime() {
        Anime animeToUpdate = AnimeCreator.createValidUpdatedAnime();
        testClient.put()
                .uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToUpdate))
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    @DisplayName("update returns error")
    public void updateAnimeError() {
        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Mono.empty());
        Anime animeToUpdate = AnimeCreator.createValidUpdatedAnime();
        testClient.put()
                .uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToUpdate))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }

}
