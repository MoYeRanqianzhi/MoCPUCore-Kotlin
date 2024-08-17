class ProgramCounter {
    var pos = 0
    private var jumped = false

    fun next(): Any = if (jumped) {
        jumped = false
    } else {
        pos ++
    }

    fun jump(pos: Int) {
        this.pos = pos
        jumped = true
    }
}