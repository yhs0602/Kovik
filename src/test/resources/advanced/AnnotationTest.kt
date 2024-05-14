package com.example.sample

// Defines an annotation for logging purposes
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Loggable(val message: String)


// Applying annotation to a class and a method
@Loggable("Logging at the class level")
class AnnotationExample {

    @Loggable("Method execution tracking")
    fun doSomething() {
        println("Doing something...")
    }
}

// Processing annotations using reflection
fun processAnnotations() {
    val classAnnotation = AnnotationExample::class.annotations.find { it is Loggable } as Loggable?
    classAnnotation?.let {
        println("Class Annotation Message: ${it.message}")
    }

    val methodAnnotation = AnnotationExample::class.members.find { it.name == "doSomething" }
        ?.annotations?.find { it is Loggable } as Loggable?
    methodAnnotation?.let {
        println("Method Annotation Message: ${it.message}")
    }
}

fun annotationDoTest() {
    val example = AnnotationExample()
    example.doSomething()
    processAnnotations()
}
