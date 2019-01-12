package com.questmark.input

/**
 * Represents one of the four cardinal directions.
 *
 * @author Ming Li
 */
enum class Direction constructor(private val dir: Int) {

    Up(0),
    Down(1),
    Left(2),
    Right(3);

    fun toNumeric(): Int {
        return dir
    }

    companion object {
        fun getDir(dir: Int): Direction? {
            for (d in Direction.values()) {
                if (d.toNumeric() == dir) return d
            }
            return null
        }
    }

}
