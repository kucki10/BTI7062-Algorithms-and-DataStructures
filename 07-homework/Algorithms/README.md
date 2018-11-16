# QuickSort #

## Intro
This program was created as a homework assignment at the module BTI7055 Algorithms-and-Datastructures at the BFH.

## Authors
Christian Nussbaum \<christian.nussbaum@protonmail.ch\> <br>
Luca Berger \<kucki10@hotmail.com\>

## Source Code
The source code can be seen on the public repostiory:
https://github.com/kucki10/BTI7062-Algorithms-and-DataStructures/tree/master/07-homework/Algorithms

## Instructions
Execute these instructions inside the extracted folder in "code".

### Build

```
> mkdir bin
> javac -d bin -sourcepath src src/main/*.java
> mkdir bin/main/view
> cp src/main/view/Application.fxml bin/main/view/Application.fxml
```

### Generate JAR-File
```
> cd bin
> jar cfe Application.jar main.Main .
> java -jar Application.jar
```


### Already builded version
Under the folder "code/bin/prebuilt", there is already a builded version. <br />
You just need to rename the extension to "jar". <br />
If the bin folder is empty, the Mail Server removed the built.
You can also download the builded version under:
https://github.com/kucki10/BTI7062-Algorithms-and-DataStructures/tree/master/07-homework/Algorithms/bin <br />


### Lessons learned
- We have the problem, that the calculation for 0 & n (bounds) took unnecessarily long. <br />
  - In order to solve this problem, we could simply execute the calculation in first iteration also for the boundaries (left & right)
  - E.g. log2(1) = 0, took very long, because the method we use is an approximation to 0. (Where left bound is always zero)
  - Therefore 0 should be checked in first iteration, to avoid this unnecessary computations.
- The algorithm is very performant (E.g. 60 iterations for log2(100000))