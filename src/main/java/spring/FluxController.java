package spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxController {

    @GetMapping("/flux")
    public Flux<String> getFlux() {
         Flux<String> flux = Flux.just("Hello", "World", "from", "Spring", "Flux");
         flux.subscribe(System.out::println);
        return flux;
    }

    public static void main(String[] args) {
        Flux<String> flux = Flux.just("Hello", "World", "from", "Spring", "Flux");
        //flux.subscribe(System.out::println);
        // windowUntil 根据给定的Predicate对流中的元素进行分组，每组元素被发射为一个新的Flux
        // 条件谓词用于确定是否结束当前窗口并开始新的窗口, 遇到满足条件的原则则分隔一个窗口，当前窗口关闭,开始一个新的窗口 [Hello][World][from, Spring][Flux]
        // 为true时, 出现的第一个窗口为空, 如何处理 @TODO
        //flux.windowUntil(item -> item.length() > 4, true).flatMap(Flux::collectList).subscribe(System.out::println);
        // 取flux中的第一个元素
        //Mono<String> mono = flux.next();
        //mono.subscribe(item -> System.out.println("first ele " + item),
        //    error -> System.out.println("Error:" + error),
        //    () -> System.out.println("completed"));

    }
}
