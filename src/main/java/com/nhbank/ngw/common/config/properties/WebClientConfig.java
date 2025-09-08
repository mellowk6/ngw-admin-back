package com.nhbank.ngw.common.config.properties;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(NgwProperties.class)
@RequiredArgsConstructor
public class WebClientConfig {

    private final NgwProperties props;

    /**
     * NGW 호출 전용 WebClient
     */
    @Bean
    public WebClient ngwWebClient(WebClient.Builder builder) {
        // Reactor Netty 클라이언트: 커넥션 타임아웃, 읽기/쓰기 타임아웃 적용
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.getTimeout().getConnectMillis())
                // 서버가 응답을 늦게 주는 경우 전체 요청의 응답 타임아웃
                .responseTimeout(Duration.ofMillis(props.getTimeout().getReadMillis()))
                .doOnConnected(conn -> conn
                        // 소켓 레벨 read/write timeout 핸들러
                        .addHandlerLast(new ReadTimeoutHandler(props.getTimeout().getReadMillis(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(props.getTimeout().getReadMillis(), TimeUnit.MILLISECONDS))
                );

        WebClient.Builder b = builder
                .baseUrl(props.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeaders(h -> {
                    h.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));
                    h.setContentType(MediaType.APPLICATION_JSON);
                    // 선택: NGW에 서비스 키를 헤더로 전달하고 싶다면
                    if (props.getServiceKey() != null && !props.getServiceKey().isBlank()) {
                        h.set("X-Service-Key", props.getServiceKey());
                    }
                })
                // 선택: 간단한 요청/응답 로깅 필터 (개발용)
                .filter(logRequest())
                .filter(logResponse());

        return b.build();
    }

    // ====== 개발 중 유용한 로깅 필터 (필요 없으면 제거) ======
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(req -> {
            // 너무 시끄러우면 줄이세요
            System.out.printf("[NGW-REQ] %s %s%n", req.method(), req.url());
            return reactor.core.publisher.Mono.just(req);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(res -> {
            System.out.printf("[NGW-RES] %s %s%n",
                    res.statusCode(),
                    res.headers().asHttpHeaders().getContentType()
            );
            return reactor.core.publisher.Mono.just(res);
        });
    }
}