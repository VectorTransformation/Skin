package skin.extension

import kotlin.system.measureTimeMillis

fun test(function: () -> Unit) {
    measureTimeMillis {
        function()
    }.apply {
        println("${decimalFormat()} ms")
    }
}