package com.tes.frezzmart

import androidx.core.util.Preconditions.checkArgument
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FunctionUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun formatDate() {


        val cal = Calendar.getInstance()
        val strDate ="2021-09-16T16:03:48Z".take(10)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date = sdf.parse(strDate)
        cal.time = date


        val formatter =  SimpleDateFormat("MMM dd'"+getDayOfMonthSuffix(cal.get(Calendar.DAY_OF_MONTH))+"', yyyy")
        val output = formatter.format(date)
        System.out.println(output)

    }

    private fun getDayOfMonthSuffix(n: Int): String {
        checkArgument(n in 1..31, "illegal day of month: $n")
        return if (n in 11..13) {
            "th"
        } else when (n % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}