import javassist.ClassPool
import javassist.NotFoundException
import java.io.File
import java.util.jar.JarFile

class DependencyValidator (private val mainClassName : String, private val jarDirectory : String, private val jarFiles: List<String>) {
    private var mainClassPackage: String

    private val pool: ClassPool = ClassPool.getDefault()

    init {
        jarFiles.forEach { pool.insertClassPath("$jarDirectory/$it") }

        val split = mainClassName.split(".")
        mainClassPackage = "${split[0]}.${split[1]}.${split[2]}"
    }

    fun validateDependencies() : Boolean {
        val availableDependencies = getAllAvailableDependencies()

        val processedClasses : MutableSet<String> = mutableSetOf()
        val necessaryDependencies : Set<String>

        try {
            necessaryDependencies = collectClassDependencies(mainClassName, processedClasses)
        } catch (e : NotFoundException) {
            return false
        }

        for (necessaryDependency in necessaryDependencies) {
            if (necessaryDependency !in availableDependencies) {
                return false
            }
        }
        return true
    }

    private fun collectClassDependencies(className: String?, visitedClasses : MutableSet<String>): Set<String> {
        val ctClass = pool.get(className)

        val classDependencies : MutableSet<String> = mutableSetOf()

        val dependencies: MutableCollection<String>? = ctClass.getRefClasses()

        if (dependencies != null) {
            for (dependency in dependencies) {
                if (dependency.startsWith("java.") || dependency.startsWith("javax.")) {
                    continue
                }
                if (dependency.contains(mainClassPackage)) {
                    if (!visitedClasses.contains(dependency)) {
                        visitedClasses.add(dependency)

                        classDependencies.addAll(
                            collectClassDependencies(dependency, visitedClasses)
                        )
                    }
                } else {
                    classDependencies.add(dependency.substringBeforeLast("."))
                }
            }
        }
        return classDependencies
    }

    private fun getAllAvailableDependencies() : MutableSet<String> {
        val dependencies : MutableSet<String> = mutableSetOf()
        jarFiles.forEach {
            dependencies.addAll(
                extractDependenciesFromJar(it)
            )
        }
        return dependencies
    }

    private fun extractDependenciesFromJar(jarName : String) : MutableSet<String> {
        var dependencies: MutableSet<String> = mutableSetOf()

        try {
            val jarFile = JarFile(File("$jarDirectory\\$jarName"))
            val manifest = jarFile.manifest
            val attributes = manifest.mainAttributes

            if (manifest != null && attributes != null) {
                val depString = attributes.getValue("Export-Package")

                if (depString != null) {
                    dependencies = depString.split(",").map { it.substringBefore(";version=") }.toMutableSet()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dependencies
    }
}
