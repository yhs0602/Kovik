package com.example.sample

open class B

open class C: B()

open class D: C()

open class E: D()

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
}