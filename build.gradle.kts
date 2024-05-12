plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.yhs0602"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.ow2.asm") {
            useVersion("9.5")
        }
    }
}

dependencies {
    // https://mvnrepository.com/artifact/org.objenesis/objenesis
    implementation("org.objenesis:objenesis:3.4")
    implementation("cglib:cglib:3.3.0")
    // https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy
    implementation("net.bytebuddy:byte-buddy:1.14.15")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}