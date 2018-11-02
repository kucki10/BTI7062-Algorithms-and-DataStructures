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



## Lessons learned
- Using the average of multiple tries for same task with same data, removes the outliers <br />
  (This makes the different sort algorithms more comparable)
   
- **InsertionSort as base**
  - InsertionSort as base method is the fastest of these three algorithms.
  - 
   
- **First try of multithreaded QuickSort**
  - The multithreaded implementation was even slower than the singlethreaded version.
  - This must be due to the fact, that the overhead of generating / managing threads is just too heavy.
  - **Note:** The implementation always used a thread if there was one available (ThreadPool)
  
- **Multithreaded QuickSort after optimization**
  - The multithreaded implementation is now sometimes a bit faster than the singlethreaded version.
  - **But:** It is not much faster than the singlethreaded solution.
  - This is due to the fact, that in the first iteration of the Quicksort all elements will be compared once. <br /> 
    (This happens always on one thread & is much work to do) <br />
    After the first iteration, the following iterations will run in separate threads, and could therefore save some time.
  - **Note:** The implementation does now only start a thread if there is one available (ThreadPool) AND the compare length is within some limit