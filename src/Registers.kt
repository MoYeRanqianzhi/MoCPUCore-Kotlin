import java.io.InputStream
import java.io.PrintStream

class Registers(
    private val pc: ProgramCounter = ProgramCounter(),
    private val `in`: InputStream = System.`in`,
    private val out: PrintStream = System.out
) {
    private val registers = Array<Int>(254) { 0 }

    operator fun get(index: Int): Int = when (index) {
        254 -> pc.pos
        255 -> `in`.read()
        else -> registers[index]
    }

    operator fun set(index: Int, value: Int) {
        when (index) {
            254 -> pc.jump(value)
            255 -> {
                out.write(value)
                out.flush()
            }
            else -> registers[index] = value
        }
    }
}