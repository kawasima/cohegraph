package net.unit8.cohegraph;

import graphql.language.Field;
import graphql.language.SelectionSet;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FieldSelectionConverter {
    public static FieldSelection fromGraphQLField(Field field) {
        List<FieldSelection> children = Collections.emptyList();
        SelectionSet selectionSet = field.getSelectionSet();
        if (selectionSet != null) {
            children = selectionSet.getSelections().stream()
                    .filter(Field.class::isInstance)
                    .map(Field.class::cast)
                    .map(FieldSelectionConverter::fromGraphQLField)
                    .collect(Collectors.toList());
        }
        return new FieldSelection(field.getName(), children);
    }
}
