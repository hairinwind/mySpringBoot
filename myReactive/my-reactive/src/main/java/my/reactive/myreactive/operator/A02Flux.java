package my.reactive.myreactive.operator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class A02Flux {
    public static void main(String[] args) {
        System.out.println("Main thread is " + Thread.currentThread().getId());

        fluxInterval();

        fluxParallel(); // this is running on another parallel thread

        fluxParallelMultipleThreads(); // running on multiple threads

    }

    private static void fluxInterval() {

        Disposable disposable = Flux.interval(Duration.ofMillis(100))
                .subscribe(e -> System.out.println("Thread " + Thread.currentThread().getId() + ": Received data ---> " + e));

        //事件的发布与消费是在相同的线程中
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("by default, Flux.interval is running on a parallel thread, not the main thread");

        disposable.dispose();
    }

    private static void fluxParallel() { // this one is not working...
        System.out.println("\nfluxParallel");
        Flux.just(1, 5)
                .publishOn(Schedulers.parallel())
                .doOnNext(System.out::println)
                .doOnComplete(() -> log.info("We can see onNext happens on one thread but not the main thread"))
                .log()
                .subscribe();

        try {
            TimeUnit.MILLISECONDS.sleep(100); // 让主线程等一会儿
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void fluxParallelMultipleThreads() {
        System.out.println("\nfluxParallel on multiple threads");
        Flux.range(1, 5)
                .parallel(2) // 2 threads in parallel
                .runOn(Schedulers.parallel())
                .doOnComplete(() -> log.info("We can see onNext happens on 2 different threads other than main thread"))
                .log()
                .subscribe();

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
