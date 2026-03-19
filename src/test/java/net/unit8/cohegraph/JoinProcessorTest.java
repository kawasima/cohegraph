package net.unit8.cohegraph;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JoinProcessorTest {

    public static class SampleModel implements Serializable {
        private final Long id;
        private final Long parentId;
        private final List<Long> childIds;
        private Object parent;
        private List<Object> children;

        public SampleModel(Long id, Long parentId, List<Long> childIds) {
            this.id = id;
            this.parentId = parentId;
            this.childIds = childIds;
        }

        public Long getId() { return id; }
        public Long getParentId() { return parentId; }
        public List<Long> getChildIds() { return childIds; }
        public Object getParent() { return parent; }
        public void setParent(Object parent) { this.parent = parent; }
        public List<Object> getChildren() { return children; }
        public void setChildren(List<Object> children) { this.children = children; }
    }

    @Test
    void getId_returnsSingleId() {
        JoinProcessor processor = new JoinProcessor("parent", "parents", "getParentId", "setParent");
        SampleModel model = new SampleModel(1L, 42L, List.of(10L, 20L));

        Object id = processor.getId(model);

        assertThat(id).isEqualTo(42L);
    }

    @Test
    void getId_returnsListOfIds() {
        JoinProcessor processor = new JoinProcessor("children", "childCache", "getChildIds", "setChildren");
        SampleModel model = new SampleModel(1L, 42L, List.of(10L, 20L));

        Object id = processor.getId(model);

        assertThat(id).isInstanceOf(List.class);
        @SuppressWarnings("unchecked")
        List<Long> idList = (List<Long>) id;
        assertThat(idList).containsExactly(10L, 20L);
    }

    @Test
    void setProperty_setsSingleValue() {
        JoinProcessor processor = new JoinProcessor("parent", "parents", "getParentId", "setParent");
        SampleModel model = new SampleModel(1L, 42L, List.of());
        String parentValue = "parentObject";

        processor.setProperty(model, parentValue);

        assertThat(model.getParent()).isEqualTo("parentObject");
    }

    @Test
    void setProperty_setsList() {
        JoinProcessor processor = new JoinProcessor("children", "childCache", "getChildIds", "setChildren");
        SampleModel model = new SampleModel(1L, 42L, List.of());
        List<Object> children = List.of("child1", "child2");

        processor.setProperty(model, children);

        assertThat(model.getChildren()).containsExactly("child1", "child2");
    }

    @Test
    void getName_returnsFieldName() {
        JoinProcessor processor = new JoinProcessor("parent", "parents", "getParentId", "setParent");

        assertThat(processor.getName()).isEqualTo("parent");
    }

    @Test
    void getCacheName_returnsCacheName() {
        JoinProcessor processor = new JoinProcessor("parent", "parents", "getParentId", "setParent");

        assertThat(processor.getCacheName()).isEqualTo("parents");
    }

    @Test
    void getId_throwsForInvalidGetter() {
        JoinProcessor processor = new JoinProcessor("parent", "parents", "nonExistentMethod", "setParent");
        SampleModel model = new SampleModel(1L, 42L, List.of());

        assertThatThrownBy(() -> processor.getId(model))
                .isInstanceOf(RuntimeException.class);
    }
}
