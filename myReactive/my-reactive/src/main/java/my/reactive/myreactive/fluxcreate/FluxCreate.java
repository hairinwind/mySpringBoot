package my.reactive.myreactive.fluxcreate;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.Scanner;

/**
 * 测试异步的 Flux.create
 * 以用户输入作为数据源
 */
public class FluxCreate {

    public static void main(String[] args) {
        MyEventSource eventSource = new MyEventSource();

        Flux<MyEventSource.MyEvent> myFlux = Flux.create(sink -> {
            eventSource.register((new MyEventListener() {
                @Override
                public void onNewEvent(MyEventSource.MyEvent event) {
                    sink.next(event);
                }

                @Override
                public void onEventStopped() {
                    sink.complete();
                }
            }));
        });

        eventSource.newEvent(new MyEventSource.MyEvent(new Date(), "event before subscribe..."));

        myFlux.subscribe(new BaseSubscriber<MyEventSource.MyEvent>() {
            private Subscription subscription;

            @Override
            public void hookOnSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(1);
                this.subscription = s;
            }

            @Override
            public void hookOnNext(MyEventSource.MyEvent myEvent) {
                System.out.println("subscriber received event:" + myEvent.toString());
                // here to put the business logic when subscriber received data
                // 这里是同步的等待 business logic 处理完成
                // 如果是异步的，就要异步 callback 回来再 request
                this.subscription.request(1); // 消费一条数据，再要一条数据
            }

            @Override
            public void hookOnError(Throwable t) {

            }

            @Override
            public void hookOnComplete() {
                System.out.println("subscriber received onComplete");
            }
        });

        eventSource.newEvent(new MyEventSource.MyEvent(new Date(), "event1 after subscribe..."));
        eventSource.newEvent(new MyEventSource.MyEvent(new Date(), "event2 after subscribe..."));

        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("input message: ");
            String msg = scanner.nextLine();

            if (msg.equals("exit()")) {
                eventSource.eventStopped();
                break;
            } else {
                eventSource.newEvent(new MyEventSource.MyEvent(new Date(), msg));
            }
        }
    }

}
