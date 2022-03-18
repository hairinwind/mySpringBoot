package my.reactive.myreactive.pubsub;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;


public class MySubscriber {

    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4, 5).subscribe(new BaseSubscriber<Integer>() {

            @Override
            public void hookOnSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(6);   // 2
            }

            @Override
            public void hookOnNext(Integer integer) {
                System.out.println("subscriber received:" + integer);
                // here to put the business logic when subscriber received data
            }

            @Override
            public void hookOnError(Throwable t) {

            }

            @Override
            public void hookOnComplete() {
                System.out.println("subscriber received onComplete");
            }
        });
    }

}
