package net.unit8.cohegraph.example;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.SchemaParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GraphQLApplication {
    @Bean
    public SchemaParser schema(GraphQLQueryResolver resolver) {
        return SchemaParser.newParser()
                .file("graphql/schema.graphqls")
                .resolvers(resolver)
                .build();
    }

    @Bean
    public GraphQLQueryResolver queryResolver() {
        return new CoheGraphQueryResolver();
    }

    public static void main(String[] args) {
        SpringApplication.run(GraphQLApplication.class, args);
    }
}
