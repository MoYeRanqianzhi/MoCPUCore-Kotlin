import java.io.File

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        val m = MoCPU(tick = 100)
        val file = File(args[0])

        if (file.exists()) {
            val bytes = file.readBytes()

            val code = mutableListOf<Array<Int>>()
            for (i in bytes.indices step 4) {
                code.add(
                    bytes.slice(i..<i + 4).map { it.toInt() and 0xFF }.toTypedArray()
                )
            }

            m.run(code)
        } else {
            println("\u001B[31m无法运行不存在的文件啊喵~~~\u001B[0m")
        }

    } else {
        println("\u001B[31mMCore Java版 强悍无比!!!\n有任何问题请联系: MoYeRanQianZhi@gmail.com\u001B[0m")
    }

//    m.run(
//        listOf(
////            arrayOf(192, 72, 0, 255),
////            arrayOf(192, 101, 0, 255),
////            arrayOf(192, 108, 0, 255),
////            arrayOf(192, 108, 0, 255),
////            arrayOf(192, 111, 0, 255),
////            arrayOf(192, 32, 0, 255),
////            arrayOf(192, 87, 0, 255),
////            arrayOf(192, 111, 0, 255),
////            arrayOf(192, 114, 0, 255),
////            arrayOf(192, 108, 0, 255),
////            arrayOf(192, 100, 0, 255),
////            arrayOf(192, 33, 0, 255),
//        )
//    )
}