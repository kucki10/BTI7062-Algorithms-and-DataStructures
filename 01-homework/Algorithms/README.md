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

### Lessons learned
- Memoized is way faster, because it has now a linear execution time
  (Because it reuses previously computed values)
  (Amount of calculations shrink)
- Threaded version is even slower than normal computation, because of the overhead
- In memoized algorithm we used the Hashcode of the values to store them in the cache. (Nice solution)
  (No Wrappers needed)
- On memoized solution it makes more sense to start with (n-2) than (n-1) 
  because this will result earlier in a cacheable result.
- Sketch before coding can help the understanding of the problem!