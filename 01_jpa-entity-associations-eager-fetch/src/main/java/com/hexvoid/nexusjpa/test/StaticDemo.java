package com.hexvoid.nexusjpa.test;

/**
 * Demonstrates static and final fields, access control, instance block,
 * static block, and encapsulation in Java.
 */
public class StaticDemo {

    // --------------------------------------------
    // 1. FIELDS (Variables)
    // --------------------------------------------

    /**
     * Private instance variable.
     * - Can only be accessed within this class.
     * - Promotes encapsulation.
     * - Accessed via getter method.
     */
    private int privateValue = 88;

    /**
     * Protected instance variable.
     * - Accessible within the same package and by subclasses.
     */
    protected int protectedValue = 55;

    /**
     * Final instance variable.
     * - Immutable after initialization.
     * - Must be assigned a value at declaration or in constructor.
     */
    final int finalValue = 97;


    // --------------------------------------------
    // 2. STATIC BLOCK
    // --------------------------------------------

    /**
     * Static block executes once when the class is loaded into memory.
     * - Used for class-level initialization tasks.
     * - Executes before any object is created or static methods are accessed.
     */
    static {
        System.out.println("Inside the Static Block of StaticDemo");
    }


    // --------------------------------------------
    // 3. INSTANCE BLOCK
    // --------------------------------------------

    /**
     * Instance initialization block.
     * - Executes every time an object of the class is created.
     * - Runs before the constructor.
     * - Commonly used to write shared initialization logic for all constructors.
     */
    {
        System.out.println("Inside the Instance Block of StaticDemo");
    }


    // --------------------------------------------
    // 4. GETTER METHOD
    // --------------------------------------------

    /**
     * Public getter for the private field `privateValue`.
     * - This method provides controlled access to a private field.
     * - Demonstrates the encapsulation principle.
     *
     * @return value of privateValue
     */
    public int getPrivateValue() {
        return privateValue;
    }


    // --------------------------------------------
    // 5. OPTIONAL: CONSTRUCTOR (Not Present)
    // --------------------------------------------
    // If a constructor is defined, it would be called after the instance block.
    // Example:
    // public StaticDemo() {
    //     System.out.println("Inside Constructor of StaticDemo");
    // }

}
