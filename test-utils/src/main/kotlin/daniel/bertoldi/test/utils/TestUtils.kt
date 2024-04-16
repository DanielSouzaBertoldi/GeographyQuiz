package daniel.bertoldi.test.utils

import java.lang.IllegalArgumentException
import java.util.UUID
import kotlin.random.Random

val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun randomUUID() = UUID.randomUUID().toString()

fun randomString(minSize: Int = 1, maxSize: Int = 5) = (minSize..maxSize).map {
    alphabet.random()
}.joinToString("")

inline fun <reified T> randomList(numOfItems: Int = 3): List<T> {
    return mutableListOf<T>().apply {
        repeat(numOfItems) {
            when (T::class) {
                Int::class -> add(Random.nextInt() as T)
                Double::class -> add(Random.nextDouble() as T)
                Float::class -> add(Random.nextFloat() as T)
                String::class -> add(randomString() as T)
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }
}

fun randomBoolean() = Random.nextBoolean()

fun randomInt(min: Int = 3, max: Int = 5) = Random.nextInt(from = min, until = max)
fun randomFloat() = Random.nextFloat()

fun randomUrl(
    scheme: String = listOf("http", "https").random(),
    domain: String = listOf("randomurl.com", "randomurl.net", "randomurl.com.br").random(),
    path: String = "",
) = "$scheme://$domain/$path"

enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
}

inline fun <reified T: Enum<T>> randomEnumValue(exceptions: List<T> = emptyList()) =
    enumValues<T>().toList().filterNot { it in exceptions }.random()