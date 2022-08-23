import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import org.apache.commons.io.output.ByteArrayOutputStream
import org.cadixdev.gradle.licenser.LicenseExtension
import org.cadixdev.gradle.licenser.Licenser

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")

    alias(libs.plugins.shadow)
    alias(libs.plugins.licenser)
    alias(libs.plugins.lombok)

    eclipse
    idea
}

fun commitHash(): String {
    val stdout = ByteArrayOutputStream()
    project.exec {
        commandLine = "git rev-parse main".split(" ")
        standardOutput = stdout
    }
    var hash: String? = null
    hash = String(stdout.toByteArray()).trim()
    if (hash == null || hash == "") {
        hash = "000000"
    }
    return hash
}

group = "fun.surviv.proxy"
version = "1.0-SNAPSHOT"
description = "Proxy System for surviv.fun"

val commit: String? = commitHash()

repositories {
    mavenCentral()
    maven { url = uri("https://plugins.gradle.org/m2/") }
    mavenLocal()
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
        mavenLocal()
    }

    apply {
        plugin<JavaPlugin>()
        plugin<JavaLibraryPlugin>()
        plugin<MavenPublishPlugin>()
        plugin<ShadowPlugin>()
        plugin<Licenser>()
        plugin<SigningPlugin>()
        plugin<EclipsePlugin>()
        plugin<IdeaPlugin>()
        plugin("io.freefair.lombok")
    }

    tasks.compileJava.configure {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    configurations.all {
        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
    }

    configure<LicenseExtension> {
        header(rootProject.file("HEADER.txt"))
        include("**/*.java")
        newLine.set(true)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        //withSourcesJar()
        //withJavadocJar()
    }

    val javaComponent = components["java"] as AdhocComponentWithVariants
    javaComponent.withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
        skip()
    }

    signing {
        if (!version.toString().endsWith("-SNAPSHOT")) {
            val signingKey: String? by project
            val signingPassword: String? by project
            useInMemoryPgpKeys(signingKey, signingPassword)
            signing.isRequired
            sign(publishing.publications)
        }
    }

    if (System.getProperty("publishName") != null && System.getProperty("publishPassword") != null) {
        publishing {
            (components["java"] as AdhocComponentWithVariants).withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
                skip()
            }
            publications {
                create<MavenPublication>(project.name) {
                    from(components["java"])
                    pom {
                        name.set(project.name)
                        url.set("https://github.com/surviv-fun/SurvivalProxy")
                        properties.put("inceptionYear", "2021")
                        licenses {
                            license {
                                name.set("General Public License (GPL v3.0)")
                                url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                                distribution.set("repo")
                            }
                        }
                        developers {
                            developer {
                                id.set("LuciferMorningstarDev")
                                name.set("Lucifer Morningstar")
                                email.set("contact@surviv.fun")
                            }
                        }
                    }
                }
                repositories {
                    maven("https://repo.surviv.fun/repository/maven-snapshot/") {
                        this.name = "surviv-snapshot"
                        credentials {
                            this.password = System.getProperty("publishPassword")
                            this.username = System.getProperty("publishName")
                        }
                    }
                }
            }
        }
    }

    tasks {

        withType<ProcessResources> {
            filesMatching("*") {
                expand(project.properties)
            }
        }

        compileJava {
            options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "1000"))
            options.compilerArgs.add("-Xlint:all")
            for (disabledLint in arrayOf("processing", "path", "fallthrough", "serial")) options.compilerArgs.add("-Xlint:$disabledLint")
            options.isDeprecation = true
            options.encoding = Charsets.UTF_8.name()
        }

        jar {
            this.archiveClassifier.set(null as String?)
            this.archiveFileName.set("${project.name}-${project.version}-${commit}.${this.archiveExtension.getOrElse("jar")}")
            this.destinationDirectory.set(file("$projectDir/../out/unshaded"))
        }

        processResources {
            filteringCharset = Charsets.UTF_8.name()
        }

        named("build") {
            dependsOn(named("shadowJar"))
        }
    }

}
