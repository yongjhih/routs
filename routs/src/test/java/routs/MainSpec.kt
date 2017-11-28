package routs

//import kotlin.test.assertEquals
//import kotlin.test.todo
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.util.*
import java.util.regex.Pattern


@RunWith(JUnitPlatform::class)
class MainSpec : Spek({
    describe("Main") {
        it("should be named paths") {
            val matcher = PathMatcher()
            matcher.add("home/:uuid<([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89ABab][0-9a-fA-F]{3}-[0-9a-fA-F]{12})>/u/:uid<([a-fA-F0-9]{12})>/v2")
            matcher.matches("home/ffffffff-ffff-4fff-afff-ffffffffffff/u/ffffffffffff/v2")
            assertThat(matcher.namedPath.get("uuid")).isEqualTo("ffffffff-ffff-4fff-afff-ffffffffffff")
            assertThat(matcher.namedPath.get("uid")).isEqualTo("ffffffffffff")
        }
        it("benchmark") {
            // 123456789012
            val UID_REGEX_V1 = "([a-fA-F0-9]{12})";
            // 12345678-1234-4321-9876-123456789012
            // 12345678-1234-4123-9123-123456789012
            // UUID
            val UUID_REGEX_V1 = "([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})";
            // home/12345678-1234-4321-9876-123456789012/u/123456789012/v1
            val patterns = arrayOf(
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v1"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v2"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v3"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v4"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v5"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v6"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v7"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v8"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v9"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v10"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v11"),
                    Pattern.compile("(^)home/" + UUID_REGEX_V1 + "/u/" + UID_REGEX_V1 + "/v12")
            )

            val paths = arrayOf(
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v1",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v2",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v3",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v4",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v5",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v6",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v7",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v8",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v9",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v10",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v11",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v12",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v13",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v14",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v15",
                    "home/12345678-1234-4321-9876-123456789012/u/123456789012/v16"
            )

            val matcher = PathMatcher()
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v1")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v2")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v3")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v4")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v5")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v6")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v7")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v8")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v9")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v10")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v11")
            matcher.add("home/:uuid<([0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12})>/u/<([a-fA-F0-9]{12})>/v12")
            //matcher.add(paths)
            //matcher.dump()
            println(LocalDateTime.now())
            for (i in 1..100000) {
                for (path in paths) {
                    if (matcher.matches(path)) {
                    }
                }
            }
            println(LocalDateTime.now())

            println(LocalDateTime.now())
            for (i in 1..100000) {
                for (pattern in patterns) {
                    for (path in paths) {
                        val matcher = pattern.matcher(path)
                        if (matcher.matches()) {
                        }
                    }
                }
            }
            println(LocalDateTime.now())
        }
    }
})

fun <T> assert(consumer: (T) -> Unit): (T) -> Boolean {
    return {
        consumer.invoke(it)
        true
    }
}
