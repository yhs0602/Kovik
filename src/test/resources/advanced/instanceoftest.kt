package com.example.sample

open class B

open class C: B()

open class D: C()

open class E: D()

interface I1

interface I2

interface I3: I1

open class F1: I1

open class F2: I2

open class F3: I3

class F4: F1()

class F5: F2()

class F6: F3(), I2

fun testInstanceOf() {
    val b = B()
    val c = C()
    val d = D()
    val e = E()

    assert(b is B)
    assert(c is C)
    assert(d is D)
    assert(e is E)

    assert(b !is C)
    assert(c !is D)
    assert(d !is E)

    assert(b !is E)
    assert(c !is E)
    assert(d !is E)

    assert(e is D)
    assert(e is C)
    assert(e is B)

    assert(d is C)
    assert(d is B)

    assert(c is B)

    println("All tests passed!")

    val f1 = F1()
    val f2 = F2()
    val f3 = F3()
    val f4 = F4()
    val f5 = F5()
    val f6 = F6()

    assert(f1 is I1)
    assert(f2 is I2)
    assert(f3 is I3)
    assert(f3 is I1)
    assert(f4 is I1)
    assert(f5 is I2)
    assert(f6 is I2)

    assert(f1 !is I2)
    assert(f2 !is I1)
    assert(f3 is I1)
    assert((f4 as Any) !is I2)
    assert((f5 as Any) !is I1)
    assert(f6 is I1)

    println("All tests passed!")
}