import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpSSE {

    private static final String SSE_URL = "https://example.com/chat/stream"; // 替换为你的 SSE 服务地址
    private static final OkHttpClient client = new OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // 禁用读取超时，适用于长连接
        .build();

    public static void main(String[] args) {
        // 创建 Flowable 来处理 SSE 数据流
        Flowable<String> sseFlowable = createSSEFlowable(SSE_URL);

        // 订阅 Flowable，模拟处理 OpenAI 风格的流式对话响应
        sseFlowable
            .subscribeOn(Schedulers.io()) // 在 I/O 线程中处理
            .observeOn(Schedulers.single()) // 在单线程中消费
            .doOnSubscribe(subscription -> System.out.println("Subscribed to SSE"))
            .doOnCancel(() -> System.out.println("Unsubscribed from SSE"))
            .subscribe(
                event -> System.out.println("Received event: " + event),
                throwable -> System.err.println("Error: " + throwable),
                () -> System.out.println("SSE stream completed")
            );

        // 为了演示，主线程等待一段时间后退出
        try {
            Thread.sleep(30000); // 等待 30 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个 Flowable，用于接收 SSE 数据流，并支持背压
     *
     * @param url SSE 服务的 URL
     * @return Flowable<String>，每个字符串是一个 SSE 消息
     */
    private static Flowable<String> createSSEFlowable(String url) {
        return Flowable.create(emitter -> {
            // 构建 SSE 请求
            Request request = new Request.Builder()
                .url(url)
                .build();

            // 创建一个新的 Call
            Call call = client.newCall(request);

            // 执行异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (!emitter.isCancelled()) {
                        emitter.onError(e); // 将错误传递给 Flowable
                    }
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (!response.isSuccessful()) {
                        if (!emitter.isCancelled()) {
                            emitter.onError(new IOException("Unexpected response code: " + response.code()));
                        }
                        return;
                    }

                    // 获取 Response 的 body
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            BufferedSource source = responseBody.source();
                            while (!source.exhausted() && !emitter.isCancelled()) {
                                String line = source.readUtf8Line();
                                if (line != null && !line.isEmpty()) {
                                    // 解析 SSE 数据，只处理 "data:" 开头的行
                                    if (line.startsWith("data:")) {
                                        String data = line.substring(5).trim(); // 去掉 "data:" 前缀
                                        emitter.onNext(data); // 推送数据到 Flowable
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        if (!emitter.isCancelled()) {
                            emitter.onError(e);
                        }
                    }

                    // 完成事件流
                    if (!emitter.isCancelled()) {
                        emitter.onComplete();
                    }
                }
            });

            // 当 Flowable 被取消订阅时，取消 HTTP 请求
            emitter.setCancellable(call::cancel);
        }, BackpressureStrategy.BUFFER); // 使用 BUFFER 策略处理背压
    }
}
