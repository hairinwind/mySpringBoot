package my.reactive.myreactive.compare;

import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MockService {

    private static List<String> ids = Arrays.asList("1", "2", "3", "4", "5");
    private static Map<String, String> idNameMap = new HashMap<>();
    private static Map<String, Integer> idStatMap = new HashMap<>();
    private static Random random = new Random();

    static {
        idNameMap.put("1", "Andy");
        idNameMap.put("2", "Benny");
        idNameMap.put("3", "Clare");
        idNameMap.put("4", "Daniel");
        idNameMap.put("5", "Ella");

        idStatMap.put("1", 101);
        idStatMap.put("2", 102);
        idStatMap.put("3", 103);
        idStatMap.put("4", 104);
        idStatMap.put("5", 105);
    }

    public static CompletableFuture<List<String>> getIds() {
        return CompletableFuture.supplyAsync(() -> ids);
    }

    public static Flux<String> getIdFlux() {
        return Flux.fromIterable(ids);
    }

    public static CompletableFuture<String> getName(String id) {
        return CompletableFuture.supplyAsync(() -> {
            int waitTime = random.nextInt(2000);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("getName(" + id + ") wait " + waitTime + "ms");
            return idNameMap.get(id);
        });
    }

    public static CompletableFuture<Integer> getStat(String id) {
        return CompletableFuture.supplyAsync(() -> {
            int waitTime = random.nextInt(2000);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("getStat(" + id + ") wait " + waitTime + "ms");
            return idStatMap.get(id);
        });
    }
}
