import com.yhs0602.dex.DexFile
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.Environment
import com.yhs0602.vm.GeneralMockedClass
import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.executeMethod
import com.yhs0602.vm.instance.MockedInstance
import java.io.PrintStream
import java.nio.file.Paths
import kotlin.jvm.internal.Intrinsics
import kotlin.test.Test

class SimpleOperationTest {
    @Test
    fun testReadFile() {
        val path = Paths.get("src/test/resources/advanced/")
        val inputDirectory = path.toFile()
        println(inputDirectory.absolutePath)
        val dexes = inputDirectory.listFiles { _, name ->
            name.endsWith(".dex")
        } ?: error("No dex files found")
        val parsedDexes = dexes.map {
            DexFile.fromFile(it)
        }
        val packageName = "com.example.sample"
        val packageNameStr = packageName.replace(".", "/")
        val classes = parsedDexes.flatMap { it.classDefs }
        val className = "TargetMethods"
        val classNameStr = "L$packageNameStr/$className;"
        val classDef = classes.find {
            it.classDef.typeId.descriptor == classNameStr
        } ?: error("No class found")
        val methods = classDef.classData?.run {
            directMethods + virtualMethods
        } ?: error("No methods found")
        val methodName = "doTest"
        val method = methods.find {
            it.methodId.name == methodName
        } ?: error("No method found")
        val codeItem = method.codeItem ?: error("No code found")
        val args = Array(codeItem.insSize.toInt()) {
            RegisterValue.ObjectRef(
                TypeId("com/example/sample/TargetMethods;"), MockedInstance(
                    Object::class.java
                )
            ) // To simply pass anything
        }
        val mockedClassesList = listOf(
            GeneralMockedClass(StringBuilder::class.java),
            GeneralMockedClass(PrintStream::class.java),
            GeneralMockedClass(System::class.java),
            GeneralMockedClass(Intrinsics::class.java),
            GeneralMockedClass(Object::class.java),
        )
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
}