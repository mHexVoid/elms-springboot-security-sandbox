package com.hexvoid.nexusjpa.test;

/**
 * Demonstrates:
 * - Access to private, protected, and final fields
 * - The role of getter methods for encapsulation
 */
public class Main {
    public static void main(String[] args) {
        // Creating an object of StaticDemo to access its members
        StaticDemo demoInstance = new StaticDemo();

        // Cannot access private field directly (Uncommenting below line causes compilation error)
        // int privateValue = demoInstance.privateValue;

        // Accessing private field via public getter method
        int privateValue = demoInstance.getPrivateValue();

        // Accessing protected and final fields directly (same package access)
        int protectedValue = demoInstance.protectedValue;
        int finalValue = demoInstance.finalValue;

        // Output the results
        System.out.println("Final Value        : " + finalValue);
        System.out.println("Protected Value    : " + protectedValue);
        System.out.println("Private Value (via getter): " + privateValue);
    }
}
