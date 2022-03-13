package my.reactive.myreactive.compare;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReactorSolution {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        Mono<List<String>> idListMono = Mono.fromCompletionStage(MockService.getIds());
        Flux<String> ids = idListMono.flatMapIterable(list -> list).log();

        Flux<String> combinations =
                ids.flatMap(id -> { // <2>
                    Mono<String> nameTask = Mono.fromCompletionStage(MockService.getName(id)); // CompletableFuture implementes CompletionStage
                    Mono<Integer> statTask = Mono.fromCompletionStage(MockService.getStat(id));

                    return nameTask.zipWith(statTask, // <5>
                            (name, stat) -> "Name " + name + " has stats " + stat);
                });

        Mono<List<String>> result = combinations.collectList();

        List<String> results = result.block(); // for testing, block here to get the result

        long end = System.currentTimeMillis();
        System.out.println("\ntime used: " + (end - start) + "ms\n");

        for (String s : results) {
            System.out.println(s);
        }
    }

}
