package com.hexvoid.nexusjpa.test;

/**
 * Parent class demonstrating:
 * - Static block: Runs once during class loading.
 * - Instance block: Runs before constructor during object creation.
 * - Variable access levels: private, protected, final.
 * - Method behavior in inheritance: static, final, and instance methods.
 */
public class ParentClass {
    
    // Instance variables (non-static fields)
    private int privateValue = 88;  // Not accessible outside this class
    protected int protectedValue = 55;  // Accessible in child classes
    final int finalValue = 97;  // Cannot be modified after assignment

    // Static block (Executed once when the class is loaded)
    static {
        System.out.println("Inside the Static Block of ParentClass");
    }

    // Instance block (Executed before constructor for every object creation)
    {
        System.out.println("Inside the Non-Static Block of ParentClass");
    }

    // Static method (Belongs to the class, not overridden in inheritance)
    static void staticMethod() {
        System.out.println("Static Method in ParentClass");
    }

    // Final method (Cannot be overridden)
    final void finalMethod() {
        System.out.println("Final Method in ParentClass");
    }

    // Instance method (Can be overridden)
    void instanceMethod() {
        System.out.println("Instance Method in ParentClass");
    }
}
