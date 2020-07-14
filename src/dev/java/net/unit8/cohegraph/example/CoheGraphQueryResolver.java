package net.unit8.cohegraph.example;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import net.unit8.cohegraph.GraphQLEntryProcessor;
import net.unit8.cohegraph.example.model.Author;
import net.unit8.cohegraph.example.model.Book;

import java.util.Collection;

public class CoheGraphQueryResolver implements GraphQLQueryResolver {
    public Collection<Book> books(DataFetchingEnvironment environment) {
        GraphQLEntryProcessor<Long, Book> bookEntryProcessor
                = new GraphQLEntryProcessor<>(Book.class, environment.getMergedField().getSingleField());
        NamedCache<Long, Book> books = CacheFactory.getCache("books");
        return books.invokeAll(bookEntryProcessor).values();
    }

    public Collection<Author> authors(DataFetchingEnvironment environment) {
        GraphQLEntryProcessor<Long, Author> authorEntryProcessor
                = new GraphQLEntryProcessor<>(Author.class, environment.getMergedField().getSingleField());
        NamedCache<Long, Author> authors = CacheFactory.getCache("authors");
        return authors.invokeAll(authorEntryProcessor).values();
    }
}
