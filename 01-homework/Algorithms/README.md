# Fibonacci #

## Intro
This program was created as a homework assignment at the module BTI7055 Algorithms-and-Datastructures at the BFH.

## Authors
Christian Nussbaum \<christian.nussbaum@protonmail.ch\> <br>
Luca Berger \<kucki10@hotmail.com\>

## Instructions

### Build
```
> mkdir bin
> javac -d bin -sourcepath src src/main/*.java
> mkdir bin\main\view
> cp src/main/view/Application.fxml bin/main/view/Application.fxml
```
### Run
```
> java -cp bin main.Main 
```

### Generate JAR-File
```
> cd bin
> jar cfe Fibonacci.jar main.Main .
> java -jar Fibonacci.jar
```
