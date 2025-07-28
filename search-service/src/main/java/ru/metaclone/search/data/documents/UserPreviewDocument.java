package ru.metaclone.search.data.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = UserPreviewDocument.INDEX_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreviewDocument {
    @Id
    private Long userId;

    @Field(type = FieldType.Text, name = LOGIN_FIELD, analyzer = "standard")
    private String login;

    @Field(type = FieldType.Text, name = FIRST_NAME_FIELD, analyzer = "standard")
    private String firstName;

    @Field(type = FieldType.Text, name = LAST_NAME__FIELD, analyzer = "standard")
    private String lastName;

    @Field(type = FieldType.Keyword)
    private String avatarUrl;

    public static final String INDEX_NAME = "userpreview";
    public static final String LOGIN_FIELD = "login";
    public static final String FIRST_NAME_FIELD = "first_name";
    public static final String LAST_NAME__FIELD = "last_name";
}