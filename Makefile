run: build
	java -cp commons-math3-3.6.1.jar:build ui.L5UserInteraction

build:
	mkdir build
	javac -cp commons-math3-3.6.1.jar src/*/*.java -d build

clean:
	rm -rf build
