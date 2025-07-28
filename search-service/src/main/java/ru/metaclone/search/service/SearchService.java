package ru.metaclone.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import ru.metaclone.search.data.documents.UserPreviewDocument;
import ru.metaclone.search.repository.UsersRepository;

import java.util.List;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.multiMatch;

@Service
@AllArgsConstructor
public class SearchService {

    private final UsersRepository usersRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public List<UserPreviewDocument> searchUsersBy(String text) {
        Query query = multiMatch(m -> m
                .query(text)
                .fields(
                        UserPreviewDocument.FIRST_NAME_FIELD,
                        UserPreviewDocument.LAST_NAME__FIELD,
                        UserPreviewDocument.LOGIN_FIELD)
                .type(TextQueryType.BestFields)
                .operator(Operator.Or)
                .fuzziness("AUTO")
        );

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<UserPreviewDocument> productHits =
                elasticsearchOperations
                        .search(nativeQuery,
                                UserPreviewDocument.class,
                                IndexCoordinates.of(UserPreviewDocument.INDEX_NAME)
                        );

        return productHits
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    public void saveUserPreview(UserPreviewDocument userPreviewDocument) {
        usersRepository.save(userPreviewDocument);
    }
}
