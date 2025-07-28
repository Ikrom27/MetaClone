package ru.metaclone.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
        basePackages = "ru.metaclone.search.repository"
)
public class ElasticsearchClientConfig {

    private final Logger logger = LoggerFactory.getLogger(ElasticsearchClientConfig.class.getName());

    @Value("${search-engine.config.host}")
    public String SEARCH_ENGINE_HOST;

    @Value("${search-engine.config.port}")
    public int SEARCH_ENGINE_PORT;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        logger.debug("========= SEARCH ENGINE INIT ============");
        logger.debug("SEARCH_ENGINE_HOST: {}", SEARCH_ENGINE_HOST);
        logger.debug("SEARCH_ENGINE_PORT: {}", SEARCH_ENGINE_PORT);
        logger.debug("=========================================");
        RestClient restClient= RestClient.builder(
                new HttpHost(SEARCH_ENGINE_HOST, SEARCH_ENGINE_PORT)
        ).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
