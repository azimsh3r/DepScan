import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

//Tests from https://github.com/zulkar/internship2024_1492
class DependencyValidatorTest {

    @Test
    fun returnsFalseWhenClassBIsMissingDependenciesWithOnlyModuleB() {
        val validator = DependencyValidator(
            mainClassName = "com.jetbrains.internship2024.ClassB",
            jarDirectory = "src/test/resources",
            jarFiles = listOf("ModuleB-1.0.jar")
        )
        assertFalse(validator.validateDependencies())
    }

    @Test
    fun returnsTrueWhenClassBHasAllRequiredDependenciesWithModuleAAndModuleB() {
        val validator = DependencyValidator(
            mainClassName = "com.jetbrains.internship2024.ClassB",
            jarDirectory = "src/test/resources",
            jarFiles = listOf("ModuleA-1.0.jar", "ModuleB-1.0.jar")
        )
        assertTrue(validator.validateDependencies())
    }

    @Test
    fun returnsTrueWhenClassAHasAllRequiredDependenciesWithOnlyModuleA() {
        val validator = DependencyValidator(
            mainClassName = "com.jetbrains.internship2024.ClassA",
            jarDirectory = "src/test/resources",
            jarFiles = listOf("ModuleA-1.0.jar")
        )
        assertTrue(validator.validateDependencies())
    }

    @Test
    fun returnsFalseWhenSomeAnotherClassIsMissingDependenciesWithOnlyModuleA() {
        val validator = DependencyValidator(
            mainClassName = "com.jetbrains.internship2024.SomeAnotherClass",
            jarDirectory = "src/test/resources",
            jarFiles = listOf("ModuleA-1.0.jar")
        )
        assertFalse(validator.validateDependencies())
    }

    @Test
    fun returnsTrueWhenSomeAnotherClassHasAllRequiredDependenciesWithModuleAAndCommonsIO() {
        val validator = DependencyValidator(
            mainClassName = "com.jetbrains.internship2024.SomeAnotherClass",
            jarDirectory = "src/test/resources",
            jarFiles = listOf("ModuleA-1.0.jar", "commons-io-2.16.1.jar")
        )
        assertTrue(validator.validateDependencies())
    }

    @Test
    fun returnsTrueWhenClassB1HasAllRequiredDependenciesWithOnlyModuleB() {
        val validator = DependencyValidator(
            mainClassName = "com.jetbrains.internship2024.ClassB1",
            jarDirectory = "src/test/resources",
            jarFiles = listOf("ModuleB-1.0.jar")
        )
        assertTrue(validator.validateDependencies())
    }
}
