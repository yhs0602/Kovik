# How to implement create-instance
Actually it is not possible to create an instance of a class with no parameters for emulation.
Therefore, create-instance actually do not create an instance. It just returns the class object.
It the call to the constructor should be done by the caller with the object. Then the class actually create the object.
This is different from the real world.
Also, the method `<clinit>` should be called, let's do it when create-instance is called. This can be different from the real world again.

# How to implement isinstance

We need to check the superclass infos from the dex file. Use superclass_idx. It is in ClassDef's superclassTypeId

```kotlin
val superClassTypeId: TypeId?,
```

Now I added interfaces lateinit var at ClassDef. Now ClassDef has both superclasses and interfaces, which means it is
sufficient to check types of the class.
Dalvik source provides useful code for type checking:
```cpp
/*
 * Perform the instanceof calculation.
 */
static inline int isInstanceof(const ClassObject* instance,
    const ClassObject* clazz)
{
    if (dvmIsInterfaceClass(clazz)) {
        return dvmImplements(instance, clazz);
    } else if (dvmIsArrayClass(instance)) {
        return isArrayInstanceOf(instance, clazz);
    } else {
        return dvmIsSubClass(instance, clazz);
    }
}
```


Dalvik source codes shows the following comments.
```cpp
/*
 * Determine whether "sub" is an instance of "clazz", where both of these
 * are array classes.
 *
 * Consider an array class, e.g. Y[][], where Y is a subclass of X.
 *   Y[][] instanceof Y[][]        --> true (identity)
 *   Y[][] instanceof X[][]        --> true (element superclass)
 *   Y[][] instanceof Y            --> false
 *   Y[][] instanceof Y[]          --> false
 *   Y[][] instanceof Object       --> true (everything is an object)
 *   Y[][] instanceof Object[]     --> true
 *   Y[][] instanceof Object[][]   --> true
 *   Y[][] instanceof Object[][][] --> false (too many []s)
 *   Y[][] instanceof Serializable     --> true (all arrays are Serializable)
 *   Y[][] instanceof Serializable[]   --> true
 *   Y[][] instanceof Serializable[][] --> false (unless Y is Serializable)
 *
 * Don't forget about primitive types.
 *   int[] instanceof Object[]     --> false
 *
 * "subElemClass" is sub->elementClass.
 *
 * "subDim" is usually just sub->dim, but for some kinds of checks we want
 * to pass in a non-array class and pretend that it's an array.
 */
```

# Marshaling
Cases of instance mocks:
1. Mocked class uses a mocked class inside emulated code
    ```java
    void foo() {
        Stringbuilder sb = new StringBuilder();
        sb.append(new StringBuilder("Hello"));    
    }
    ```

2. Mocked class uses a class inside emulated code
    ```java
    import java.util.Arrays;
    import java.util.List;
    
    class A implements Comparable {
        public int compareTo(Object o) {
            return 0;
        }
    
        static void foo() {
            var As = new A[3];
            As[0] = new A();
            As[1] = new A();
            As[2] = new A();
            Arrays.sort(As);
        }
    }
    ```
3. Emulated class uses a mocked class inside emulated code
    ```java
    import java.util.Arrays;
    import java.util.List;
    
    class A{
        static void bar() {
            System.out.println("Hello"); // uses PrintStream
        }
    }
    ```
   
4. Emulated class uses another emulated class inside emulated code
    ```java
    class A {
        static void foo() {
            B b = new B();
            b.bar();
        }
    }
    
    class B {
           void bar() {
                System.out.println("Hello");
            }
        }
    ```