
## add dependencies in pom.xml

## postgres docker-composer.yml
create docker-composer.yml to run postgres on local  
once the yml file is ready, go to the terminal and run ```docker-compose up``` to start postgres  
You shall see ```db-1  | 2024-04-29 18:56:07.141 UTC [1] LOG:  database system is ready to accept connections``` on the console  

## unit test
my.webflux.service.AnimeServiceTest  
my.webflux.controller.AnimeControllerTest  

## integration test
my.webflux.integration.AnimeControllerIT

## R2DBC transactions
turn on the log 
```dbn-psql
logging.level.org.springframework.r2dbc.connection.R2dbcTransactionManager: DEBUG
```
the log can show 
```dbn-psql
o.s.r.c.R2dbcTransactionManager          : Committing R2DBC transaction on Connection
```
or rollback
```dbn-psql
o.s.r.c.R2dbcTransactionManager          : Initiating transaction rollback
```

## do validation before saveAll
here is the code to do validation before saveAll
```dbn-psql
 return Flux.fromIterable(animes) //Flux<Anime>
                .flatMap(this::validateAnime) //after flatMap, anime is sent to  function validateAnime
                .collectList() // validateAnime returns Mono<Anime>, after collectList returns Mono<List<Anime>>
                .flatMapMany(animeRepository::saveAll);
```
- Flux.fromIterable(animes) creates a Flux from the input list of animes.
- flatMap(this::validateAnime) applies the validateAnime method to each anime in the list.
- if there is validation error, it already returned from here, no further steps.
- collectList() collectList() collects all the validated Anime objects into a Mono<List<Anime>>.
- flatMapMany(animeRepository::saveAll) convert Mono<List<Anime>> to List<Anime>, then call animeRepository::saveAll, once the call is done, return Flux<Anime>
  - flatMap returns Mono
  - flatMapMany returns Flux

## security
https://youtu.be/bL23Xbyqc7s?si=MZTf-jVxVvb_BE6U  
User from database
https://youtu.be/bL23Xbyqc7s?si=6eLNPT3qoapyP8Tm
https://youtu.be/GaMPjiqNKL8?si=eHMh8dvlIb6TZCAu

## 
