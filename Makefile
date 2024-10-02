build:
	mvn package -Dquarkus.package.type=uber-jar -D skipTests

start:
	./mvnw compile quarkus:dev