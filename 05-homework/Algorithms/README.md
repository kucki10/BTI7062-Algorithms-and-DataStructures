# MergeSort & InsertionSort #

## Intro
This program was created as a homework assignment at the module BTI7055 Algorithms-and-Datastructures at the BFH.

## Authors
Christian Nussbaum \<christian.nussbaum@protonmail.ch\> <br>
Luca Berger \<kucki10@hotmail.com\>

## Source Code
The source code can be seen on the public repostiory:
https://github.com/kucki10/BTI7062-Algorithms-and-DataStructures/tree/master/05-homework/Algorithms

## Instructions
Execute these instructions inside the extracted folder. 

### Build
On windows use  ; in -cp as separator <br />
On other OS use : in -cp as separator
```
> mkdir bin
> javac -d bin -cp ".;./lib/commons-lang3-3.0.1.jar" -sourcepath src src/main/*.java
> mkdir bin\main\view
> cp src/main/view/Application.fxml bin/main/view/Application.fxml
> cp lib/commons-lang3-3.0.1.jar bin/commons-lang3-3.0.1.jar
```
### Run
On windows use  ; in -cp as separator <br />
On other OS use : in -cp as separator
```
> java -cp "bin;bin/commons-lang3-3.0.1.jar" main.Main
```

### Generate JAR-File
```
> cd bin
> jar cvfm MergeSort.jar ../MANIFEST.mf .
> java -jar MergeSort.jar
```

### Lessons learned
- TODO
