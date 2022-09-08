package lang.java11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 〈API使用更方便 支持异步发送请求 返回一个CompletableFuture来构建异步操作通道〉<br>
 *
 * @author Carrie
 * @create 2022/9/8
 * @since 1.0.0
 */
public class HttpClientFeature {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("requestUrl"))
            .GET()
            .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // 异步执行 不会阻塞当前线程 返回一个CompletableFuture
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println);
    }
}