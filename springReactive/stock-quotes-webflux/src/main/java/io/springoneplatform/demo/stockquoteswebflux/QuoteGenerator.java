package io.springoneplatform.demo.stockquoteswebflux;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

@Component
public class QuoteGenerator {

    private final MathContext mathContext = new MathContext(2);

    private final Random random = new Random();

    private final List<Quote> prices = new ArrayList<>();

    public QuoteGenerator() {
        prices.add(new Quote("CTXS", 82.26));
        prices.add(new Quote("DELL", 63.74));
        prices.add(new Quote("GOOG", 847.24));
        prices.add(new Quote("MSFT", 65.11));
        prices.add(new Quote("ORCL", 45.71));
        prices.add(new Quote("RHT", 84.29));
        prices.add(new Quote("VMW", 92.21));
    }


    public Flux<Quote> fetchQuoteStream(Duration period) {

        return Flux.generate(() -> 0,
                   (BiFunction<Integer, SynchronousSink<Quote>, Integer>) (index, sink) -> {
                        System.out.println("index: " + index);
                       Quote updatedQuote = updateQuote(prices.get(index));
                       sink.next(updatedQuote);
                       int i = ++index % prices.size();
                       System.out.println("return " + i); // return 的 i 会作为下一次函数调用的 index, 默认一次调用这个方法 32 次，生成了 32 个 quote 对象
                       return i;
                   })
                   .zipWith(Flux.interval(period)) // zipWith 表示两个 flux 流合并
                   .map(t -> {
                       System.out.println(t.getT2()); // 流中每个对象是个 tuple， tuple 的 T2 是上面 Flux.interval 推进来的一个 Long
                       return t.getT1(); // tuple 的 T1 是 quote 对象
                   })
                   .map(quote -> {
                       quote.setInstant(Instant.now());
                       return quote;
                   });
//                   .log("io.springoneplatform.demo.stockquoteswebflux");
    }

    private Quote updateQuote(Quote quote) {
        BigDecimal priceChange =
                quote.getPrice()
                     .multiply(new BigDecimal(0.05 * random.nextDouble()), mathContext);
        return new Quote(quote.getTicker(), quote.getPrice().add(priceChange));
    }
}
