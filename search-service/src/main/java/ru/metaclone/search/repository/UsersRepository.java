package ru.metaclone.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.metaclone.search.data.documents.UserPreviewDocument;

public interface UsersRepository  extends ElasticsearchRepository<UserPreviewDocument, Long> { }
