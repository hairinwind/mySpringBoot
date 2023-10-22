package my.reactive.myreactive.operator;

import reactor.core.publisher.Mono;

public class A01Mono {

    public static void main(String[] args) {
        // this method is to prove that the callable function is executed only after subscribe
        callableExecutedAfterSubscribe();

        //defer Mono creation
        deferMono();
    }

    private static void callableExecutedAfterSubscribe() {
        Mono<String> fromCallable = Mono.fromCallable(() -> {
            System.out.println("callable is running...shall appear after subscribe");
            return "returned from callable";
        });

        System.out.println("before subscribe...");
        fromCallable.subscribe(System.out::println);
    }

    private static void deferMono() {
        System.out.println("");
        final boolean[] myFlags = new boolean[]{true, true};
        Mono<String> defer = Mono.defer(() -> myFlags[0]==myFlags[1] ? Mono.just("flag == true") : Mono.just("flag == false"));

        myFlags[1] = !myFlags[1];
        defer.subscribe((s) -> {
            System.out.println(s);
            System.out.println("it shall be flag == false, as the mono is created at subscribe time");
        });
    }

}
