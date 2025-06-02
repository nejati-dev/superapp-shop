plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.mojtaba.superapp"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// =========================
	// Spring Boot, JPA, Security, ...
	// =========================
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("com.github.ben-manes.caffeine:caffeine")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// =========================
	// JTS (کتابخانهٔ هندسه)
	// =========================
	implementation("org.locationtech.jts:jts-core:1.19.0")

	// =========================
	// Hibernate Spatial (برای PostgreSQL + JTS)
	// =========================
	implementation("org.hibernate:hibernate-spatial:6.6.13.Final")
	runtimeOnly("org.postgresql:postgresql:42.6.0")

	// =========================
	// Lombok
	// =========================
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// =========================
	// Spring DevTools
	// =========================
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// =========================
	// Dependencies تست
	// =========================
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.assertj:assertj-core:3.24.2")
	testImplementation("org.mockito:mockito-core:5.6.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.6.0")

	// نسخهٔ واحد از H2 برای تست‌های JPA
	testImplementation("com.h2database:h2:2.3.232")

	testImplementation("org.orbisgis:h2gis:2.2.3")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
