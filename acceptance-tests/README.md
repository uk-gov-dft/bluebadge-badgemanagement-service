### API Acceptance tests

#### Start the badgemanagement service

First you need to start the badgemanagement service by executing following commands

```
cd badgemanagement-service
./gradlew bootRun
```

#### How to run api acceptance tests

You need the following service to run api acceptance tests:
-authorisation service
-reference data service

You may need to install maven plugin in intellij.

Then go to api-acceptance-test project and right click on pom.xml → Add as Maven project


To run
```
./gradlew clean build acceptanceTests -DbaseUrl=http://localhost:8280
```

### Relevant Articles: 
- [Test a REST API with Java](http://www.baeldung.com/2011/10/13/integration-testing-a-rest-api/)
- [Introduction to WireMock](http://www.baeldung.com/introduction-to-wiremock)
- [REST API Testing with Cucumber](http://www.baeldung.com/cucumber-rest-api-testing)
- [Testing a REST API with JBehave](http://www.baeldung.com/jbehave-rest-testing)
- [REST API Testing with Karate](http://www.baeldung.com/karate-rest-api-testing)

