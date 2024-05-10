plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.yhs0602"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.objenesis/objenesis
    implementation("org.objenesis:objenesis:3.4")
    implementation("cglib:cglib:3.3.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}