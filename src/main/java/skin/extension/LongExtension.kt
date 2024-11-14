package skin.extension

import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,###")

fun Long.decimalFormat(): String {
    return decimalFormat.format(this)
}