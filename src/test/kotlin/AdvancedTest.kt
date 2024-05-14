import com.yhs0602.dex.DexFile
import com.yhs0602.vm.Environment
import com.yhs0602.vm.GeneralMockedClass
import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.executeMethod
import java.nio.file.Paths
import kotlin.jvm.internal.Intrinsics
import kotlin.test.BeforeTest
import kotlin.test.Test

class AdvancedTest {
    @BeforeTest
    fun setup() {
        Environment.reset()
    }

    fun testInterpreter(
        path: String,
        packageName: String,
        className: String,
        methodName: String,
        mockedClassesList: List<GeneralMockedClass>,
        args: Array<RegisterValue> = arrayOf()
    ) {
        val inputDirectory = Paths.get(path).toFile()
        println(inputDirectory.absolutePath)
        val dexes = inputDirectory.listFiles { _, name ->
            name.endsWith(".dex")
        } ?: error("No dex files found")
        val parsedDexes = dexes.map {
            DexFile.fromFile(it)
        }
        val packageNameStr = packageName.replace(".", "/")
        val classes = parsedDexes.flatMap { it.classDefs }
        val classNameStr = "L$packageNameStr/$className;"
        val classDef = classes.find {
            it.classDef.typeId.descriptor == classNameStr
        } ?: error("No class found")
        val methods = classDef.classData?.run {
            directMethods + virtualMethods
        } ?: error("No methods found")
        val method = methods.find {
            it.methodId.name == methodName
        } ?: error("No method found")
        val codeItem = method.codeItem ?: error("No code found")
        val mockedMethodList = mockedClassesList.flatMap {
            it.getMethods()
        }
        val mockedMethods = mockedMethodList.associateBy {
            Triple(it.classId, it.parameters, it.name)
        }
        val mockedClasses = mockedClassesList.associateBy {
            it.classId
        }
        val environment = Environment(
            parsedDexes,
            mockedMethods,
            mockedClasses,
            beforeInstruction = { pc, instruction, memory, depth ->
                val indentation = "    ".repeat(depth)
                println("$indentation Before $instruction: $pc// ${memory.registers.toList()} exception=${memory.exception}, returnValue = ${memory.returnValue.contentToString()}") // Debug
            },
            afterInstruction = { pc, instruction, memory, depth ->
                val indentation = "    ".repeat(depth)
                println("$indentation After $instruction: $pc// ${memory.registers.toList()} exception=${memory.exception}, returnValue = ${memory.returnValue.contentToString()}") // Debug
            }
        )
        executeMethod(codeItem, environment, args, args.size, 0)
    }

    @Test
    fun TestStatic() {
        testInterpreter(
            "src/test/resources/advanced/",
            "com.example.sample",
            "StaticExample",
            "doTest",
            listOf(
                GeneralMockedClass(StringBuilder::class.java),
                GeneralMockedClass(System::class.java),
                GeneralMockedClass(Intrinsics::class.java),
                GeneralMockedClass(Object::class.java),
            )
        )
    }
}