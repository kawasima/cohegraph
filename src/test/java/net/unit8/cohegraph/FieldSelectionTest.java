package net.unit8.cohegraph;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FieldSelectionTest {

    @Test
    void getName_returnsName() {
        FieldSelection sel = new FieldSelection("books", Collections.emptyList());
        assertThat(sel.getName()).isEqualTo("books");
    }

    @Test
    void getChildren_returnsChildren() {
        FieldSelection child1 = new FieldSelection("name", Collections.emptyList());
        FieldSelection child2 = new FieldSelection("author", Collections.emptyList());
        FieldSelection sel = new FieldSelection("books", List.of(child1, child2));

        assertThat(sel.getChildren()).hasSize(2);
        assertThat(sel.getChildren().get(0).getName()).isEqualTo("name");
        assertThat(sel.getChildren().get(1).getName()).isEqualTo("author");
    }

    @Test
    void getChildren_nullDefaultsToEmptyList() {
        FieldSelection sel = new FieldSelection("books", null);
        assertThat(sel.getChildren()).isEmpty();
    }

    @Test
    void getChild_findsExistingChild() {
        FieldSelection child = new FieldSelection("author", Collections.emptyList());
        FieldSelection sel = new FieldSelection("books", List.of(child));

        assertThat(sel.getChild("author")).isPresent();
        assertThat(sel.getChild("author").get().getName()).isEqualTo("author");
    }

    @Test
    void getChild_returnsEmptyForMissing() {
        FieldSelection sel = new FieldSelection("books", Collections.emptyList());
        assertThat(sel.getChild("nonexistent")).isEmpty();
    }

    @Test
    void nestedTree_preservesStructure() {
        FieldSelection grandchild = new FieldSelection("firstName", Collections.emptyList());
        FieldSelection child = new FieldSelection("author", List.of(grandchild));
        FieldSelection root = new FieldSelection("books", List.of(child));

        assertThat(root.getChild("author")).isPresent();
        assertThat(root.getChild("author").get().getChild("firstName")).isPresent();
    }
}
