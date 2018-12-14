# Fibonacci Matrix

## Intro
This program was created as a homework assignment at the module BTI7055 Algorithms-and-Datastructures at the BFH.

## Authors
Christian Nussbaum \<christian.nussbaum@protonmail.ch\> <br>
Luca Berger \<kucki10@hotmail.com\>

## Source Code
The source code can be seen on the public repostiory:
https://github.com/kucki10/BTI7062-Algorithms-and-DataStructures/tree/master/09-homework/Algorithms

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
> jar cfe FibonacciMatrix.jar main.Main .
> java -jar FibonacciMatrix.jar
```


### Already builded version
Under the folder "code/bin/prebuilt", there is already a builded version. <br />
You just need to rename the extension to "jar". <br />
If the bin folder is empty, the Mail Server removed the built.
You can also download the builded version under:
https://github.com/kucki10/BTI7062-Algorithms-and-DataStructures/tree/master/09-homework/Algorithms/bin <br />



## Lessons learned
- It does not matter, if the Fibonacci matrix is colummn-based or row-based
  - This is due to the fact that the matrices which will be multiplied
    - are quadratic
    - AND even symmetric
  - Also the multiplication is defined as folllows:
    - The cells of the ROW of the first matrix will be multiplied with the cells of the COLUMN of the second matrix. <br/>
    --> This means, the first matrix is used row-based, the second matrix is used column-based
  - <b>OPTIMAL</b> would be a multiplication of a row-based matrix with a column-based matrix <br />
    Because there is less "memory jumping".
- The Matrix multiplication for fibonacci has a linear complexity.
  - That's because of the matrix multiplication always happens on a 2x2 matrix. <br />
    which is independent of the input. <br />
    <b>--> Therefore the Tripple-For-Loop construct of the Matrix-calculation is just a elementary instruction!</b>
  - This elementary instruction is called n- times <br />
    while loop which decrements the counter
  - <b>Complexity = n --> linear complexity.</b>