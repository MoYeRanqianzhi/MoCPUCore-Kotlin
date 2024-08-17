import java.io.InputStream
import java.io.PrintStream
import kotlin.math.pow


class MoCPU(
    private val pc: ProgramCounter = ProgramCounter(),
    private val `in`: InputStream = System.`in`,
    private val out: PrintStream = System.out,
    private val tick: Long = 0
) {
    private val registers = Registers(pc, `in`, out)

    private val operations = mapOf<Int, (Int, Int) -> Int>(
        0 to { a, b -> (a + b) and 0xFF },
        1 to { a, b -> (a - b) and 0xFF },
        2 to { a, b -> (a * b) and 0xFF },
        3 to { a, b -> (a / b) and 0xFF },
        4 to { a, b -> (a.floorDiv(b)) and 0xFF },
        5 to { a, b -> (a % b) and 0xFF },
        6 to { a, b -> (a pow b) and 0xFF },
        7 to { a, b -> (a and b) and 0xFF },
        8 to { a, b -> (a or b) and 0xFF },
        9 to { a, b -> (a xor b) and 0xFF },
    )

    private val conditions = mapOf<Int, (Int, Int) -> Boolean>(
        32 to { _, _ -> true },
        33 to { _, _ -> false },
        34 to { a, b -> a == b },
        35 to { a, b -> a != b },
        36 to { a, b -> a < b },
        37 to { a, b -> a <= b },
        38 to { a, b -> a > b },
        39 to { a, b -> a >= b },
    )

    fun checkSyntax(opcode: Int, value1: Int, value2: Int, target: Int): Boolean = when {
        opcode > 255 -> throw MemoryOverflowError("OPCODE[$opcode] > 255, should be >= 0 and <= 255")
        opcode < 0 -> throw MemoryOverflowError("OPCODE[$opcode] < 0, should be >= 0 and <= 255")
        value1 > 255 -> throw MemoryOverflowError("VALUE1[$value1] > 255, should be >= 0 and <= 255")
        value1 < 0 -> throw MemoryOverflowError("VALUE1[$value1] < 0, should be >= 0 and <= 255")
        value2 > 255 -> throw MemoryOverflowError("VALUE2[$value2] > 255, should be >= 0 and <= 255")
        value2 < 0 -> throw MemoryOverflowError("VALUE2[$value2] < 0, should be >= 0 and <= 255")
        target > 255 -> throw MemoryOverflowError("TARGET[$target] > 255, should be >= 0 and <= 255")
        target < 0 -> throw MemoryOverflowError("TARGET[$target] < 0, should be >= 0 and <= 255")
        else -> true
    }

    fun run(code: List<Array<Int>>) {
        while (pc.pos < code.size) {
            require(code[pc.pos].size == 4) { "CODE[$pc.pos] should have 4 elements" }
            exec(code[pc.pos][0], code[pc.pos][1], code[pc.pos][2], code[pc.pos][3])
            pc.next()
            if (tick > 0) Thread.sleep(tick)
        }
    }

    private fun exec(opcode: Int, value1: Int, value2: Int, target: Int) {
        val op = opcode and 0b0011_1111
        val v1 = if (opcode and 128 == 128) value1 else registers[value1]
        val v2 = if (opcode and 64 == 64) value2 else registers[value2]

        if (op and 32 == 0) {
            cal(operations[op]!!, v1, v2, target)
        } else {
            cond(conditions[op]!!, v1, v2, target)
        }
    }

    private inline fun cal(operation: (Int, Int) -> Int, v1: Int, v2: Int, tar: Int) {
        registers[tar] = operation(v1, v2)
    }
    private inline fun cond(condition: (Int, Int) -> Boolean, v1: Int, v2: Int, tar: Int) {
        if (condition(v1, v2)) pc.jump(tar)
    }
}

class MemoryOverflowError(err: String) : Error("MemoryOverflowError: $err")

infix fun Int.pow(exponent: Int): Int = this.toDouble().pow(exponent).toInt()

