fun main(args: Array<String>) {
    var className: String? = null
    var jarPath: String? = null
    val jarList: MutableList<String> = mutableListOf()

    while (className == null) {
        print("Enter the name of the main class (e.g., com.example.MainClass): ")
        className = readlnOrNull()
    }

    while (jarPath == null) {
        print("Enter the path to the folder containing your JAR files: ")
        jarPath = readlnOrNull()
    }

    var jarFileName: String?
    do {
        print("Enter a JAR file name (press Enter when done): ")
        jarFileName = readlnOrNull()
        if (!jarFileName.isNullOrEmpty()) {
            jarList.add(jarFileName)
        }
    } while (!jarFileName.isNullOrEmpty())

    val result = DependencyValidator(className, jarPath, jarList).validateDependencies()

    if (result) {
        println("All dependencies are present.")
    } else {
        println("Some dependencies are missing.")
    }
}
