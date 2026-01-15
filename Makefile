clean:
	./mvnw clean

install:
	SKIP_PUBLISH_TO_CENTRAL=true ./mvnw clean install

.PHONY: clean install
