clean:
	./mvnw clean

install:
	./mvnw clean install

release:
	./mvnw --batch-mode clean release:prepare release:perform

.PHONY: clean install
