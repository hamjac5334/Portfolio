import random

easy = [ "frog", "book", "ship", "tree", "door", "wolf",
        "lion", "rain", "fire", "moon", "snow", "wind", "star", "bear",
        "fish", "lake", "rock", "sand", "wave", "leaf", "nest", "path", "clay",
         "vine", "seed", "root", "bark", "coal", "flax", "fern", "bush", "twig",
          "pond", "hail", "mist", "bees", "moth", "fawn", "deer", "cave", "lava",
          "reef", "herb", "worm", "mice", "crab", "hawk", "crow", "dove"]

medium =  ["planet", "animal", "garden", "splash", "forest", "bridge",
            "island", "fabric", "hunter", "puzzle", "whales", "cotton", "stream",
             "guitar", "chrome", "tunnel", "flight", "button", "artist", "flower",
             "cheese", "farmer", "police", "pencil", "orange", "trophy", "plasma",
             "museum", "circle", "castle", "magnet", "rocket", "sculpt", "clover",
             "snakes", "pigeon", "bottle", "singer", "marble", "silver", "harbor",
             "rubber", "insect", "doctor", "pebble", "stream", "shadow", "mirror",
             "canyon", "tablet" ]

hard = ["mountain", "dinosaur", "building", "computer", "elephant", "umbrella",
        "airplane", "pinecone", "baseball", "sandwich", "scissors", "butterfly",
        "elephant", "hospital", "calendar", "football", "raincoat", "magazine",
        "popcorn", "skylight", "notebook", "backpack", "firework", "necklace",
        "bookworm", "chimneys", "cucumber", "language", "notebook", "pineapple",
        "raincoat", "shoulder", "sidewalk", "stairway", "sunshine", "telescope",
        "telephone", "treasure", "vacation", "hairbrush", "snowflake", "teaspoon",
        "blueberry", "chocolate", "honeycomb", "pineapple", "sandwich", "sparkling",
        "sunflower", "waterfall" ]

def hang_man() -> str:
    errors = 7
    guesses = []
    done = False

    level = input("What level would you like to play? (easy, medium, or hard) ").lower()

    if level == "easy":
        word = random.choice(easy)
    elif level == "medium":
        word = random.choice(medium)
    elif level == "hard":
        word = random.choice(hard)
    else:
        print("Invalid level. Please choose easy, medium, or hard.")
        return hang_man()

    print(f"This is the {level} level. The word is {len(word)} letters long. Good Luck!")

    while not done and errors > 0:
        display_word = ''.join([letter if letter in guesses else '_' for letter in word])
        print(f"Word: {display_word}")
        print(f"Errors left: {errors}")
        guess = input("What letter do you think is in the word? ").lower()

        if guess in guesses:
            print("You already guessed that letter.")
            continue

        guesses.append(guess)

        if guess in word:
            print(f"Good job! {guess} is in the word.")
        else:
            errors -= 1
            print(f"Sorry, {guess} is not in the word. You have {errors} errors left.")

        if all(letter in guesses for letter in word):
            print(f"Congratulations! You guessed the word: {word}")
            done = True

    if errors == 0:
        print(f"Game over! The word was: {word}")

    again = input("Would you like to play again? (y/n) ").lower()
    if again == 'y':
        hang_man()
    else:
        print("Thanks for playing!")
        quit()

if __name__ == "__main__":
    hang_man()		
					
					
					
			
			
		
		



