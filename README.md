# Cohegraph

## Examples

1. Build cohegraph and examples.
```
% mvn compile
```
2. Start a coherence server
```
% mvn -PcoherenceServer exec:java
```
3. Set up example data
```
% mvn -PdataSetup exec:java
```
4. Start the GraphQL server.
```
% mvn spring-boot:run
```
5. Check `http://localhost:9000/graphiql` in your browser.
