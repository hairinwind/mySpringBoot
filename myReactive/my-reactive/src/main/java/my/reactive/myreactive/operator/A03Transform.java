package my.reactive.myreactive.operator;

import reactor.core.publisher.Flux;

import java.util.function.Function;

public class A03Transform {
    public static void main(String[] args) {
        withoutTransform();

        withTransform();
    }

    private static void withoutTransform() {
        Flux.range(1, 10)
                .filter(v -> v % 3 == 0)
                .filter(v -> v % 2 == 0)
                .map(v -> v + 1)
                .map(v -> v * v)
                .subscribe(System.out::println);
    }

    private static void withTransform() {
        Function<Flux<Integer>, Flux<Integer>> filterAndMap =
                f -> f.filter(v -> v % 3 == 0 && v % 2 == 0)
                        .map(v -> (v+1)*(v+1));
        Flux<Integer> transformFlux = Flux.range(1, 10)
                .transform(filterAndMap);
        transformFlux.subscribe(System.out::println);

        transformFlux.subscribe(System.out::println);
    }
}
