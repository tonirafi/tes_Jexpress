package com.template.android

import androidx.core.util.Preconditions.checkArgument
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
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
        val strDate = "2021-09-16T16:03:48Z".take(10)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date = sdf.parse(strDate)
        cal.time = date


        val formatter =
            SimpleDateFormat("MMM dd'" + getDayOfMonthSuffix(cal.get(Calendar.DAY_OF_MONTH)) + "', yyyy")
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

@Test
    fun tes(){
        // FIZZ BUZZ
        // FIZZ BUZZ
        for (num in 1..100) {
            if (num % 3 == 0 && num % 5 == 0) {
                println("FizzBuzz")
            } else if (num % 3 == 0) {
                println("Fizz")
            } else if (num % 5 == 0) {
                println("Buzz")
            } else if (isPrime(num) === true) {
                println("$num is prime")
            } else {
                println(num)
            }
        }
    }

    fun isPrime(num: Int): Boolean {
        if (num == 1) return false
        for (i in 2 until num) {
            if (num % i == 0) return false
        }
        return true
    }
    fun getMinRoof(cars: List<*>, k: Int, size: Long): Long {
        if (cars.size <= 0 || k <= 0 || size < k) return Long.MAX_VALUE
        Collections.sort(cars)
        var min = Long.MAX_VALUE
        min = Math.min(min, getMinRoof(cars, k, size - 1))
        return Math.min(min, (cars[(size - 1).toInt()] - cars[(size - k).toInt()] + 1).toLong())
    }



}