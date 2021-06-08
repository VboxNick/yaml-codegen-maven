clean:
	./mvnw clean

install:
	export GPG_TTY=$$(tty)
	./mvnw clean install -Dgpg.executable=$$(which gpg)

release:
	export GPG_TTY=$$(tty)
	./mvnw --batch-mode clean release:prepare release:perform -Dgpg.executable=$$(which gpg)

.PHONY: clean install
