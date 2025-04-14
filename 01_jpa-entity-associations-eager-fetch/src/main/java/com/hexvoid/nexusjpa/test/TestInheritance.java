package com.hexvoid.nexusjpa.test;

/**
 * Demonstrates method behavior in inheritance, including method overriding 
 * and static method hiding.
 */
public class TestInheritance {
    public static void main(String[] args) {
       
    	System.out.println("Creating ParentClass Reference Holding ChildClass Object");
        ParentClass parentReference = new ChildClass();  // Upcasting

        System.out.println("\nCalling finalMethod:");
        parentReference.finalMethod();  // Calls ParentClass method (final cannot be overridden)

        System.out.println("\nCalling instanceMethod:");
        parentReference.instanceMethod();  // Calls overridden method in ChildClass

        System.out.println("\nCalling staticMethod:");
        parentReference.staticMethod();  // Calls ParentClass static method (method hiding)

        System.out.println("\nCasting to ChildClass and Calling Unique Method:");
        ChildClass childReference = (ChildClass) parentReference;
        childReference.uniqueChildMethod();  // Calls ChildClass-specific method
    }
}
