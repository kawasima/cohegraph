package net.unit8.cohegraph.example;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import graphql.schema.DataFetchingEnvironment;
import net.unit8.cohegraph.FieldSelection;
import net.unit8.cohegraph.FieldSelectionConverter;
import net.unit8.cohegraph.GraphQLEntryProcessor;
import net.unit8.cohegraph.example.model.Author;
import net.unit8.cohegraph.example.model.Book;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;

@Controller
public class CoheGraphQueryResolver {
    @QueryMapping
    public Collection<Book> books(DataFetchingEnvironment environment) {
        FieldSelection selection = FieldSelectionConverter.fromGraphQLField(
                environment.getMergedField().getSingleField());
        GraphQLEntryProcessor<Long, Book> bookEntryProcessor
                = new GraphQLEntryProcessor<>(Book.class, selection);
        NamedCache<Long, Book> books = CacheFactory.getCache("books");
        return books.invokeAll(bookEntryProcessor).values();
    }

    @QueryMapping
    public Collection<Author> authors(DataFetchingEnvironment environment) {
        FieldSelection selection = FieldSelectionConverter.fromGraphQLField(
                environment.getMergedField().getSingleField());
        GraphQLEntryProcessor<Long, Author> authorEntryProcessor
                = new GraphQLEntryProcessor<>(Author.class, selection);
        NamedCache<Long, Author> authors = CacheFactory.getCache("authors");
        return authors.invokeAll(authorEntryProcessor).values();
    }
}
