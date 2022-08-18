import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

fun commitHash(): String {
    val stdout = org.apache.commons.io.output.ByteArrayOutputStream()
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

val commit: String? = commitHash()

dependencies {
    compileOnly(libs.velocity)
    compileOnly(libs.luckperms)
    compileOnly(libs.kyori)

    implementation(libs.gson)
    implementation(libs.mongo)
    implementation(libs.jedis)

}

tasks.named<ShadowJar>("shadowJar") {
    this.archiveClassifier.set(null as String?)
    this.archiveFileName.set("${project.name}-${project.version}.${this.archiveExtension.getOrElse("jar")}")
    this.destinationDirectory.set(file("$projectDir/../out"))
    // Get rid of all the libs which are 100% unused.
    minimize()
    mergeServiceFiles()
}
