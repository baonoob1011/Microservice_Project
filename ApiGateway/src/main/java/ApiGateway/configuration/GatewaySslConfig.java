package ApiGateway.configuration;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

@Configuration
public class GatewaySslConfig {

    @Bean
    public HttpClient httpClient() throws Exception {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE) // ⚠️ chỉ dùng dev
                .build();

        return HttpClient.create().secure(t -> t.sslContext(sslContext));
    }
}
