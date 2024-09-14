package models

enum class BoardSize(val width: Int, val height: Int) {
    EASY(2, 4),
    MEDIUM(3, 6),
    HARD(4, 6);

    val count = width * height
    val pairsCount = count / 2
}