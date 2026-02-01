plugins {
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 5 dependencies with proper launcher
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
}

tasks.test {
    useJUnitPlatform()
    
    // Ensure test output is visible
    testLogging {
        events("passed", "skipped", "failed")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "rei.Rei"
}

// Configure JAR to be executable
tasks.jar {
    archiveBaseName = "Rei"
    archiveVersion = ""
    
    manifest {
        attributes(
            "Main-Class" to "rei.Rei"
        )
    }
    
    // Include all runtime dependencies (though we don't have any in this project)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    
    // Prevent duplicates
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Configure source and test directories (they're already in standard locations)
sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
    test {
        java {
            srcDirs("src/test/java")
        }
        resources {
            srcDirs("src/test/resources")
        }
    }
}