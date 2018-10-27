# QuickSort #

## Intro
This program was created as a homework assignment at the module BTI7055 Algorithms-and-Datastructures at the BFH.

## Authors
Christian Nussbaum \<christian.nussbaum@protonmail.ch\> <br>
Luca Berger \<kucki10@hotmail.com\>

## Source Code
The source code can be seen on the public repostiory:
https://github.com/kucki10/BTI7062-Algorithms-and-DataStructures/tree/master/06-homework/Algorithms

## Instructions
Execute these instructions inside the extracted folder in "code".

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
> jar cfe Quicksort.jar main.Main .
> java -jar Quicksort.jar
```


### Already builded version
Under the folder "code/bin/prebuilt", there is already a builded version. <br />
You just need to rename the extension to "jar". <br />
If the bin folder is empty, the Mail Server removed the built.
You can also download the builded version under:
https://github.com/kucki10/BTI7062-Algorithms-and-DataStructures/tree/master/06-homework/Algorithms/bin <br />