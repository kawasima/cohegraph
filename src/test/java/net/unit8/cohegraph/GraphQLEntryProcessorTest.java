package net.unit8.cohegraph;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GraphQLEntryProcessorTest {

    public static class SimpleModel implements Serializable {
        private final Long id;
        private final String name;

        public SimpleModel(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
    }

    public static class ModelWithJoin implements Serializable {
        private final Long id;
        private final Long parentId;

        @JoinCache(name = "parents", idProperty = "parentId")
        private volatile SimpleModel parent;

        public ModelWithJoin(Long id, Long parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        public Long getId() { return id; }
        public Long getParentId() { return parentId; }
        public SimpleModel getParent() { return parent; }
        public void setParent(SimpleModel parent) { this.parent = parent; }
    }

    public static class ModelWithMultipleJoins implements Serializable {
        private final Long id;
        private final Long authorId;
        private final List<Long> tagIds;

        @JoinCache(name = "authors", idProperty = "authorId")
        private volatile SimpleModel author;

        @JoinCache(name = "tags", idProperty = "tagIds")
        private volatile List<SimpleModel> tags;

        public ModelWithMultipleJoins(Long id, Long authorId, List<Long> tagIds) {
            this.id = id;
            this.authorId = authorId;
            this.tagIds = tagIds;
        }

        public Long getId() { return id; }
        public Long getAuthorId() { return authorId; }
        public List<Long> getTagIds() { return tagIds; }
        public SimpleModel getAuthor() { return author; }
        public void setAuthor(SimpleModel author) { this.author = author; }
        public List<SimpleModel> getTags() { return tags; }
        public void setTags(List<SimpleModel> tags) { this.tags = tags; }
    }

    private FieldSelection selection(String name, FieldSelection... children) {
        return new FieldSelection(name, Arrays.asList(children));
    }

    @Test
    void constructor_noJoinAnnotations_createsEmptyProcessors() {
        FieldSelection sel = selection("simple");

        GraphQLEntryProcessor<Long, SimpleModel> processor =
                new GraphQLEntryProcessor<>(SimpleModel.class, sel);

        assertThat(processor).isNotNull();
    }

    @Test
    void constructor_withSingleJoinAnnotation_createsOneProcessor() {
        FieldSelection sel = selection("model");

        GraphQLEntryProcessor<Long, ModelWithJoin> processor =
                new GraphQLEntryProcessor<>(ModelWithJoin.class, sel);

        assertThat(processor).isNotNull();
    }

    @Test
    void constructor_withMultipleJoinAnnotations_createsMultipleProcessors() {
        FieldSelection sel = selection("model");

        GraphQLEntryProcessor<Long, ModelWithMultipleJoins> processor =
                new GraphQLEntryProcessor<>(ModelWithMultipleJoins.class, sel);

        assertThat(processor).isNotNull();
    }

    @Test
    void constructor_withNestedSelections() {
        FieldSelection sel = selection("books",
                selection("name"),
                selection("author",
                        selection("firstName"),
                        selection("lastName")),
                selection("tags",
                        selection("name")));

        GraphQLEntryProcessor<Long, ModelWithMultipleJoins> processor =
                new GraphQLEntryProcessor<>(ModelWithMultipleJoins.class, sel);

        assertThat(processor).isNotNull();
    }
}
