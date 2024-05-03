package my.webflux.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.webflux.domain.Anime;
import my.webflux.repository.AnimeRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Flux<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Mono<Anime> findById(int id) {
        return animeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")));
    }

    public Mono<Anime> save(Anime anime) {
        return animeRepository.save(anime);
    }

    public Mono<Void> update(Anime anime) {
        return findById(anime.getId())
                .map(animeFound -> anime.withId(animeFound.getId()))  //withId returns the Anime object, while setId return is void
                .flatMap(animeRepository::save) // if return here, it returns Mono<Anime>
                .then();
//                .thenEmpty(Mono.empty());      // this returns Mono<Void>
    }

    public Mono<Void> delete(int id) {
        return findById(id).flatMap(animeRepository::delete);
    }

    @Transactional
    public Flux<Anime> saveAll(List<Anime> animes) {
        // validate after save
        // this code is just for testing transaction rollback
        // as the database insert actions already happen and then rollback
//        return animeRepository
//                .saveAll(animes)
//                .doOnNext(this::throwResponseStatusExceptionWhenEmptyName);

        //validate before save
        return Flux.fromIterable(animes)
                .flatMap(this::validateAnime)
                .collectList()
                .flatMapMany(animeRepository::saveAll);
    }

    private Mono<Anime> validateAnime(Anime anime) {
        if (StringUtils.isBlank(anime.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Name");
        }
        return Mono.just(anime);
    }

    private void throwResponseStatusExceptionWhenEmptyName(Anime anime) {
        if (StringUtils.isBlank(anime.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Name");
        }
    }
}
