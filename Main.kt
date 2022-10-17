import java.io.File
import kotlin.math.max
import kotlin.math.min

fun doOffsetArr(arr: IntArray, OffSet: Int): IntArray {
    val offSet = OffSet % arr.size
    val first = arr.slice(0 until offSet)
    var index=0
    return arr.mapIndexed { idx, _ ->
        if (idx >= arr.size - offSet) first[index++] else arr[idx + offSet]
    }.toIntArray()
}

fun check(matrix: Array<IntArray>, k: Int, col: Int, row: Int): Boolean {
    val transposed = Array(row) {IntArray(col)}

    for (i in 0 until row) {
        for (j in 0 until col) {
            transposed[i][j] = matrix[j][i]
        }
    }
    var c = 0
    for (i in 0 until row) {
        for (j in 0 until col) {
            val el = transposed[i][j]
            for (l in j+1 until col) {
                if (el == transposed[i][l]) {
                    matrix[j][i] = k-c
                    c++
                }
            }
        }
    }
    println("YES")
    return true
}

fun process (n: Int, m: Int, k: Int, lamps: Array<IntArray>) {
    val matrix = lamps.copyOf()
    for (i in 0 until n) {
        var index=0
        while ((matrix[i].slice(index until matrix[i].size)).indexOf(0) != -1) {
            for (j in index until index+(matrix[i].slice(index until matrix[i].size)).indexOf(0)) {
                matrix[i][j] = j+1-index
                if (j+1-index > k) {
                    println("NO")
                    return
                }
            }
            index += matrix[i].slice(index until matrix[i].size).indexOf(0) + 1
        }
        for (j in index until matrix[i].size) {
            matrix[i][j] = j+1-index
            if (j+1-index > k) {
                println("NO")
                return
            }
        }
    }
    for (i in 0 until n) {
        var left=0
        var right=matrix[i].size
        var count = 0
        for (j in matrix[i]) { if (j == 0) count++ }
        for (zeros in 0 .. count) {
            for (idx in left+1 until matrix[i].size) {
                if (matrix[i][idx] == 0) {
                    right=idx
                    break
                }
                if (idx == matrix[i].size-1) {
                    right = idx+1
                }
            }
            val arr = doOffsetArr(matrix[i].slice(left until right).toIntArray(), i)
            for (j in left until right) {
                matrix[i][j] = arr[j-left]
            }
            left = right+1
        }
    }
    if(check(matrix, k, n, m)) {
        for (i in matrix){
            println(i.contentToString())
        }
    }
}

fun main() {
    val data = mutableListOf<String>()
    File("./src/main/kotlin/colors.txt").forEachLine {
        data.add(it)
    }
    var idx = 0
    val tests = data[idx++].toInt()
    for (test in 0 until tests) {
        val vars = data[idx++].split(' ').map { it.toInt() }.toTypedArray()
        val matrix: Array<IntArray> = Array(min(vars[0], vars[1])) {IntArray(max(vars[0], vars[1])) {0} }
        for (i in 0 until vars[0]) {
            matrix[i] = data[idx++].split(' ').map { it.toInt() }.toIntArray()

        }
        process(vars[0], vars[1], vars[2], matrix)
    }
}
