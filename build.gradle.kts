plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.serialization") version "1.9.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

group = "com"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")
val springAiVersion by extra("1.0.1")
val springCloudVersion by extra("2023.0.6")

dependencies {
    implementation("org.hibernate.orm:hibernate-vector:6.6.15.Final")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    implementation("com.google.firebase:firebase-admin:9.5.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("org.postgresql:postgresql")
    implementation("com.pgvector:pgvector:0.1.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.google.code.gson:gson:2.10.1")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)

    attributes(mapOf("snippets" to project.extra["snippetsDir"]!!))

    doFirst {
        delete(file("src/main/resources/static/docs"))
    }
}

tasks.register("copyRestdocsToStatic") {
    group = "documentation"
    description = "Copy generated REST Docs to static resources for OpenAPI JSON generation"
    dependsOn(tasks.asciidoctor)

    doLast {
        copy {
            from(file("build/docs/asciidoc"))
            into(file("src/main/resources/static/docs"))
        }
    }
}

tasks.register("prepareDocsForGithubPages") {
    group = "documentation"
    description = "Prepare documentation for GitHub Pages deployment"
    dependsOn(tasks.asciidoctor)

    doLast {
        copy {
            from(file("build/docs/asciidoc"))
            into(file("docs"))
        }
    }
}

tasks.bootJar {
    archiveBaseName.set(rootProject.name)
    archiveVersion.set("")
}
