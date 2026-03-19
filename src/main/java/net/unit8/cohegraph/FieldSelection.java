package net.unit8.cohegraph;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FieldSelection implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<FieldSelection> children;

    public FieldSelection(String name, List<FieldSelection> children) {
        this.name = name;
        this.children = children != null ? children : Collections.emptyList();
    }

    public String getName() {
        return name;
    }

    public List<FieldSelection> getChildren() {
        return children;
    }

    public Optional<FieldSelection> getChild(String name) {
        return children.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst();
    }
}
