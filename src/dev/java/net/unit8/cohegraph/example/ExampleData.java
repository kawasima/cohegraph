package net.unit8.cohegraph.example;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedMap;
import net.unit8.cohegraph.example.model.Author;
import net.unit8.cohegraph.example.model.Book;
import net.unit8.cohegraph.example.model.Publisher;

import java.util.List;

public class ExampleData {
    private static void dataSetup() {
        NamedMap<Long, Author> authors = CacheFactory.getCache("authors");
        NamedMap<Long, Book> books = CacheFactory.getCache("books");
        NamedMap<Long, Publisher> publishers = CacheFactory.getCache("publishers");

        Author martinFowler = new Author(1L, "Martin", "Fowler", List.of(1L, 2L));
        Author kentBeck = new Author(2L, "Kent", "Beck", List.of(3L, 4L));
        Book refactoring = new Book(1L, "Refactoring", 300, 1L, 1L);
        Book pofeaa = new Book(2L, "Patterns of Enterprise Application Architecture", 400, 1L, 1L);
        Book junitPocketGuide = new Book(3L, "Junit Pocket Guide", 200, 2L, 2L);
        Book extremeProgramming = new Book(4L, "Extreme Programming Explained", 500, 2L, 1L);
        Publisher addisonWesley = new Publisher(1L, "Addison-Wesley Professional");
        Publisher oreilly = new Publisher(2L, "O'Reilly");

        authors.put(martinFowler.getId(), martinFowler);
        authors.put(kentBeck.getId(), kentBeck);
        books.put(refactoring.getId(), refactoring);
        books.put(pofeaa.getId(), pofeaa);
        books.put(junitPocketGuide.getId(), junitPocketGuide);
        books.put(extremeProgramming.getId(), extremeProgramming);
        publishers.put(addisonWesley.getId(), addisonWesley);
        publishers.put(oreilly.getId(), oreilly);
    }

    public static void main(String[] args) {
        dataSetup();
        /*
        GraphQLProvider graphQLProvider = new GraphQLProvider(Thread.currentThread().getContextClassLoader().getResource("schema.graphqls"));
        GraphQL graphQL = graphQLProvider.getGraphQL();
        String query = "{ books { id name author { firstName lastName }}}";
        ExecutionResult result = graphQL.execute(query);
        System.out.println("query=" + query);
        System.out.println("result=" + result.getData().toString());

        query = "{ authors { firstName lastName books { name publisher { name }} }}";
        result = graphQL.execute(query);
        System.out.println("query=" + query);
        System.out.println("result=" + result.getData().toString());
         */
    }
}
