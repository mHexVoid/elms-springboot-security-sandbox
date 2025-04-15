package com.hexvoid.nexusjpa.test;

/**
 * ChildClass extends ParentClass and demonstrates:
 * 
 * - Method Overriding (instance methods)
 * - Method Hiding (static methods)
 * - Class-specific behavior
 */
public class ChildClass extends ParentClass {

    /**
     * Overrides the instance method from ParentClass.
     * Demonstrates true polymorphism.
     */
    @Override
    void instanceMethod() {
        super.instanceMethod(); // Optional: invokes parent implementation
        System.out.println("Overridden Instance Method in ChildClass");
    }

    /**
     * Hides the static method of ParentClass.
     * 
     * Note: This is not polymorphism — which method is invoked
     * depends on the reference type, not the object.
     */
    static void staticMethod() {
        System.out.println("Static Method in ChildClass (Hiding ParentClass Method)");
    }

    /**
     * Method specific to ChildClass.
     * Not present in ParentClass.
     */
    public void uniqueChildMethod() {
        System.out.println("Method Unique to ChildClass");
    }
}
