package net.unit8.cohegraph;

import graphql.language.Field;
import graphql.language.SelectionSet;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FieldSelectionConverterTest {

    @Test
    void fromGraphQLField_leafField() {
        Field field = Field.newField("name").build();

        FieldSelection result = FieldSelectionConverter.fromGraphQLField(field);

        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getChildren()).isEmpty();
    }

    @Test
    void fromGraphQLField_withChildren() {
        Field nameField = Field.newField("name").build();
        Field pageCountField = Field.newField("pageCount").build();
        Field booksField = Field.newField("books")
                .selectionSet(new SelectionSet(List.of(nameField, pageCountField)))
                .build();

        FieldSelection result = FieldSelectionConverter.fromGraphQLField(booksField);

        assertThat(result.getName()).isEqualTo("books");
        assertThat(result.getChildren()).hasSize(2);
        assertThat(result.getChildren().get(0).getName()).isEqualTo("name");
        assertThat(result.getChildren().get(1).getName()).isEqualTo("pageCount");
    }

    @Test
    void fromGraphQLField_nestedSelections() {
        Field firstNameField = Field.newField("firstName").build();
        Field lastNameField = Field.newField("lastName").build();
        Field authorField = Field.newField("author")
                .selectionSet(new SelectionSet(List.of(firstNameField, lastNameField)))
                .build();
        Field nameField = Field.newField("name").build();
        Field booksField = Field.newField("books")
                .selectionSet(new SelectionSet(List.of(nameField, authorField)))
                .build();

        FieldSelection result = FieldSelectionConverter.fromGraphQLField(booksField);

        assertThat(result.getName()).isEqualTo("books");
        assertThat(result.getChildren()).hasSize(2);

        FieldSelection authorSelection = result.getChild("author").orElseThrow();
        assertThat(authorSelection.getChildren()).hasSize(2);
        assertThat(authorSelection.getChildren().get(0).getName()).isEqualTo("firstName");
        assertThat(authorSelection.getChildren().get(1).getName()).isEqualTo("lastName");
    }

    @Test
    void fromGraphQLField_emptySelectionSet() {
        Field field = Field.newField("books")
                .selectionSet(new SelectionSet(Collections.emptyList()))
                .build();

        FieldSelection result = FieldSelectionConverter.fromGraphQLField(field);

        assertThat(result.getName()).isEqualTo("books");
        assertThat(result.getChildren()).isEmpty();
    }
}
