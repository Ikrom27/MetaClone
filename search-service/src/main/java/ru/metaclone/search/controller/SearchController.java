package ru.metaclone.search.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.search.data.documents.UserPreviewDocument;
import ru.metaclone.search.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@AllArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/users")
    public ResponseEntity<List<UserPreviewDocument>> searchUser(
            @RequestParam(name = "query", defaultValue = "") String query
    ) {
        return ResponseEntity.ok(searchService.searchUsersBy(query));
    }
}
