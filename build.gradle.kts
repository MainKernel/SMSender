plugins {
    id("java")
    application
}

group = "org.smsender"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    /* Excel library */
    implementation ("org.apache.poi:poi:5.4.0")

    implementation("org.apache.poi:poi-ooxml-full:5.4.0")

}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.smsender.App") // Головний клас вашої програми
}

tasks {

    register<Jar>("fatJar") {
        group = "build"
        description = "Creates a fat JAR with all dependencies"


        archiveBaseName.set("SMSender")
        archiveVersion.set("") // Виключаємо версію з імені файлу


        from(sourceSets.main.get().output)


        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
        manifest {
            attributes(
                "Main-Class" to "org.smsender.App" // Замініть на ваш головний клас
            )
        }
        // Уникаємо дублювання файлів (опціонально)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

}}