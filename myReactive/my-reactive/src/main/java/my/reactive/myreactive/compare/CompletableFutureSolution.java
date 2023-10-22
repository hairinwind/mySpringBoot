package my.reactive.myreactive.compare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableFutureSolution {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        CompletableFuture<List<String>> ids = MockService.getIds();

        CompletableFuture<List<String>> result = ids.thenComposeAsync(l -> {
            Stream<CompletableFuture<String>> zip =
                    l.stream().map(i -> {
                        CompletableFuture<String> nameTask = MockService.getName(i);
                        CompletableFuture<Integer> statTask = MockService.getStat(i);

                        return nameTask.thenCombineAsync(statTask, (name, stat) -> "Name " + name + " has stats " + stat);
                    });
            List<CompletableFuture<String>> combinationList = zip.collect(Collectors.toList());
            CompletableFuture<String>[] combinationArray = combinationList.toArray(new CompletableFuture[combinationList.size()]); // 转成 array, 因为 allof 只接受 array 类型，不接受 list

            CompletableFuture<Void> allDone = CompletableFuture.allOf(combinationArray); // 等待所有完成
            return allDone.thenApply(v -> combinationList.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        });

        List<String> results = result.join(); // blocked to see the result

        long end = System.currentTimeMillis();
        System.out.println("\ntime used: " + (end - start) + "ms\n");

        for (String s : results) {
            System.out.println(s);
        }
    }
}
