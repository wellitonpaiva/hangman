import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.JettyLoom
import org.http4k.server.asServer

fun main() {
    routes(
        initial(), btnTry(), getAttempts(), check()
    ).asServer(JettyLoom(8080)).start()
}

private const val MAX_ATTEMPTS = 6

val hangman = mapOf(
    0 to hangman0,
    1 to hangman1,
    2 to hangman2,
    3 to hangman3,
    4 to hangman4,
    5 to hangman5,
    MAX_ATTEMPTS to hangman6,
)
var attempts = 0

var words = listOf("SHIP", "SHEEP", "CHEAP", "CHIP")
var guess = guess("A")

data class Guess(val char: String, val guessed: Boolean)

fun guess(word: String): List<Guess> = word.map { Guess(it.toString(), false) }

fun initial() = "/" bind Method.GET to {
    attempts = 0
    guess = guess(words.random())
    Response(OK).body(createHTML().html {
        head {
            script(type = ScriptType.textJScript, src = "https://unpkg.com/htmx.org@2.0.1") {}
            link(rel = "stylesheet", href = "https://cdn.simplecss.org/simple.min.css")
        }
        body {
            h1 { +"Hangman" }
            div {
                id = "attempts"
                attempts("You have $MAX_ATTEMPTS left")
            }
            div {
                id = "attempt"
                table {
                    tr {
                        guess.map {
                            td {
                                classes = setOf("guess${it.char}")
                                h2(block = guessField(it))
                            }
                        }
                    }
                }
            }
            keyboard()
        }
    }
    )
}

private fun guessField(it: Guess): H2.() -> Unit = {
    attributes["hx-post"] = "/check/${it.char}"
    attributes["hx-trigger"] = "click from:.${it.char} delay:1s"
    attributes["target"] = ".guess${it.char}"
    +if (it.guessed) it.char else "_"
}

private fun BODY.keyboard() {
    val keyboard = arrayOf(
        arrayOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        arrayOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        arrayOf("Z", "X", "C", "V", "B", "N", "M")
    )
    table {
        keyboard.forEach { r ->
            tr {
                r.forEach { key ->
                    td {
                        div {
                            keyButton(key)
                        }
                    }
                }

            }
        }
    }
}

private fun DIV.attempts(msg: String) {
    div {
        attributes["hx-get"] = "/attempts"
        attributes["hx-trigger"] = "click from:.keyboard-button delay:1s"
        attributes["target"] = "#attempts"
        h2 { +msg }
        div(block = hangman[attempts]!!)
    }
}

private fun DIV.keyButton(key: String) = div {
    id = "btn$key"
    button {
        classes = setOf("keyboard-button", key)
        //attributes["hx-trigger"] = "click, keyup[key=='${key.lowercase()}'||key=='${key.uppercase()}'] from:body"
        attributes["hx-trigger"] = "click"
        attributes["hx-post"] = "/try/$key"
        attributes["hx-target"] = "#btn$key"
        +key
    }
}

fun btnTry() = "/try/{key}" bind Method.POST to { req ->
    val key = req.path("key")!!
    if (!guess.map { it.char }.contains(key)) {
        attempts += 1
    }
    Response(OK).body(createHTML().button {
        disabled = true
        +key
    })
}

fun check() = "/check/{key}" bind Method.POST to { req ->
    val key = req.path("key")!!
    guess = guess.map { if (it.char == key) Guess(it.char, true) else it }.toList()
    Response(OK).body(createHTML().h2(block = guessField(Guess(key, true))))
}

fun getAttempts() = "/attempts" bind Method.GET to {
    if (attempts >= MAX_ATTEMPTS) {
        Response(OK).body(createHTML().div { attempts("You lost!") })
    } else {
        if (guess.filter { it.guessed }.size == guess.size) {
            Response(OK).body(createHTML().div { attempts("You won!") })
        } else {
            Response(OK).body(createHTML().div { attempts("You have ${MAX_ATTEMPTS - attempts} ${if (MAX_ATTEMPTS - attempts == 1) "attempt" else "attempt"} left") })
        }
    }
}
