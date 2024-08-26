import kotlinx.html.DIV
import kotlinx.html.pre

val hangman0: DIV.() -> Unit = {
    pre {
        +"""  +---+
  |   |
      |
      |
      |
      |
========="""
    }
}

val hangman1: DIV.() -> Unit = {
    pre {
        +"""  +---+
  |   |
  O   |
      |
      |
      |
========="""
    }
}

val hangman2: DIV.() -> Unit = {
    pre {
        +"""  +---+
  |   |
  O   |
  |   |
      |
      |
========="""
    }
}

val hangman3: DIV.() -> Unit = {
    pre {
        +"""  +---+
  |   |
  O   |
 /|   |
      |
      |
========="""
    }
}

val hangman4: DIV.() -> Unit = {
    pre {
        +"""  +---+
  |   |
  O   |
 /|\  |
      |
      |
========="""
    }
}

val hangman5: DIV.() -> Unit = {
    pre {
        +"""  +---+
  |   |
  O   |
 /|\  |
 /    |
      |
========="""
    }
}
val hangman6: DIV.() -> Unit = {
    pre {
        +"""  +---+
  |   |
  O   |
 /|\  |
 / \  |
      |
========="""
    }
}

