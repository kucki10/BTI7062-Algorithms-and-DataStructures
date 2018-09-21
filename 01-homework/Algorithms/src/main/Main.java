package main;

import algorithms.algorithms.helper.ExecutionTimer;
import algorithms.examples.DynamicallyFibonacciDnC;
import algorithms.examples.FibonacciDnC;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        executeNormalFibonacci();

        printNewLines();

        executeDynamicallyFibonacci();

        printNewLines();


    }

    private static void printNewLines() {
        System.out.println("");
        System.out.println("");
    }


    private static void executeNormalFibonacci() {
        for (long i = 20; i < 30; i++) {
            FibonacciDnC fib = new FibonacciDnC(i);

            ExecutionTimer timer = new ExecutionTimer(() -> {
                return fib.divideAndConquer();
            });

            System.out.println("Fibonnaci of " + i + " is: " + timer.result + " (took " + timer.time + " ns)");
        }
    }

    private static void executeDynamicallyFibonacci() {
        for (long i = 20; i < 30; i++) {
            Map<Integer, Long> cache = new HashMap<>();

            DynamicallyFibonacciDnC fib = new DynamicallyFibonacciDnC(i);

            ExecutionTimer timer = new ExecutionTimer(() -> {
                return fib.divideAndConquer(cache);
            });

            System.out.println("Memoized Fibonnaci of " + i + " is: " + timer.result + " (took " + timer.time + " ns)");
        }
    }
}
