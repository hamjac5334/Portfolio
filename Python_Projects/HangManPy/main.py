def hang_man() -> str:

#easy function
    def easy() -> str:
        word = "bath"
        print("This is the easy level. The word is 4 letters long!!!")
        errors = 7
        guesses = []
        done = False
        while not done: 
            for letter in word:
                if letter.lower() in guesses:
                    print(letter, end=" ")
                else:
                    print("_", end=" ")
            print("")

            guess = input(f"You have {errors} guesses left, next guess: ")
            guesses.append(guess.lower())
            if guess.lower() not in word.lower():
                errors -= 1
                if errors == 0:
                    print("You Lost!!! :(")
                    again = input("Would you like to play again?")
                    if again == "y":
                        hang_man()
                    else:
                         quit()
            done = True
            for letter in word:
                if letter.lower() not in guesses:
                    done = False
        again = input("Would you like to play again?")
        if again == "y":
            hang_man()
        else:
            quit()
#medium function
    def medium() -> str:
        word = "tragic"
        print("This is the medium level. The word is 7 letters long!!!")
        allowed_errors = 7
        guesses = []
        done = False

        while not done:
            for letter in word:
                if letter.lower() in guesses:
                    print(letter, end=" ")
                else:
                    print("_", end=" ")
            print("")

            guess = input(f"You have {allowed_errors} guesses left, next guess: ")
            guesses.append(guess.lower())
            if guess.lower() not in word.lower():
                allowed_errors -= 1
                if allowed_errors == 0:
                    print("You Lost!!! :(")
                    again = input("Would you like to play again?")
                    if again == "y":
                        hang_man()
                    else:
                        quit()

            done = True
            for letter in word:
                if letter.lower() not in guesses:
                    done = False

        again = input("Would you like to play again?")
        if again == "y":
            hang_man()
        else:
            quit()
#difficult function
    def difficult() -> str:
        print("This is the difficult level. The word is 8 letters long!!!")
        word = "zambonees"
        allowed_errors = 7
        guesses = []
        done = False

        while not done:
            for letter in word:
                if letter.lower() in guesses:
                    print(letter, end=" ")
                else:
                    print("_", end=" ")
            print("")

            guess = input(f"You have {allowed_errors} guesses left, next guess: ")
            guesses.append(guess.lower())
            if guess.lower() not in word.lower():
                allowed_errors -= 1
                if allowed_errors == 0:
                    print("You Lost!!! :(")
                    again = input("Would you like to play again?")
                    if again == "y":
                        hang_man()
                    else:
                        quit()

            done = True
            for letter in word:
                if letter.lower() not in guesses:
                    done = False
        again = input("Would you like to play again?")
        if again == "y":
            hang_man()
        else:
            quit()

#Asks user what level they want to play
    level = input("Which level would you like to play: easy, medium, or difficult")
    if level == "easy":
        easy()
    elif level == "medium":
        medium()
    elif level == "difficult":
        difficult()
    else:
        print("Sorry, that's not an option")

hang_man()