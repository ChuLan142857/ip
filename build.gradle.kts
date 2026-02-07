plugins {
    application
    java
    id("org.openjfx.javafxplugin") version "0.0.14"
}

repositories {
    mavenCentral()
}

dependencies {
    val javaFxVersion = "17.0.7"
    
    // JavaFX base dependencies (without classifier for compilation)
    implementation("org.openjfx:javafx-base:$javaFxVersion")
    implementation("org.openjfx:javafx-controls:$javaFxVersion")
    implementation("org.openjfx:javafx-fxml:$javaFxVersion")
    implementation("org.openjfx:javafx-graphics:$javaFxVersion")
    
    // Platform-specific runtime dependencies for cross-platform distribution
    runtimeOnly("org.openjfx:javafx-base:$javaFxVersion:win")
    runtimeOnly("org.openjfx:javafx-base:$javaFxVersion:mac")
    runtimeOnly("org.openjfx:javafx-base:$javaFxVersion:linux")
    runtimeOnly("org.openjfx:javafx-controls:$javaFxVersion:win")
    runtimeOnly("org.openjfx:javafx-controls:$javaFxVersion:mac")
    runtimeOnly("org.openjfx:javafx-controls:$javaFxVersion:linux")
    runtimeOnly("org.openjfx:javafx-fxml:$javaFxVersion:win")
    runtimeOnly("org.openjfx:javafx-fxml:$javaFxVersion:mac")
    runtimeOnly("org.openjfx:javafx-fxml:$javaFxVersion:linux")
    runtimeOnly("org.openjfx:javafx-graphics:$javaFxVersion:win")
    runtimeOnly("org.openjfx:javafx-graphics:$javaFxVersion:mac")
    runtimeOnly("org.openjfx:javafx-graphics:$javaFxVersion:linux")
    
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

// Handle duplicate resources
tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "rei.Launcher"
}

javafx {
    version = "17.0.7"
    modules = listOf("javafx.controls", "javafx.fxml")
}

// Configure JAR to be executable
tasks.jar {
    archiveBaseName = "Rei"
    archiveVersion = ""
    
    manifest {
        attributes(
            "Main-Class" to "rei.Launcher"
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