all: build


build:
	rm -rf target/*.jar
	mvn package

install:
	cp target/JYT-*-jar-with-dependencies.jar /Applications/JYT.app/Contents/Resources/bin/JYT.jar

clean:
	mvn clean
