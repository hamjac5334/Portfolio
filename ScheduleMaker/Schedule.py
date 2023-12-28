#https://docs.python.org/3/library/calendar.html
#https://www.tutorialspoint.com/python_data_structure/python_2darray.htm

from typing import *
import calendar
toDo = []
time = []

def main():
    year = int(input("Please Enter a Year (as an int): "))
    month = int(input("Please Enter a Month (as an int): "))
    print(calendar.month(year, month))
    day = int(input("Please Enter a Day (as an int): "))
    print(f" On {month} {day} {year} you have {toDo} to do." )

def add():
    things = True
    while things == True:
        questionAdd =input("Would you like to add anything to your to-do list? ( y or n)")
        if questionAdd == "y":
            adding = input("What would you like to add? ")
            timing = input("What time do you want this to be? ")
            toDo.append(adding)
            time.append(timing)
            print(print_numbered_list())
        elif questionAdd == "n":
            print(print_numbered_list())
            things = False

def print_numbered_list():
    i = 0
    while i < len(toDo):
        print("-------------------")
        print(str(i + 1) + ") " + time[i] + " " + toDo[i])
        i += 1
    print("-------------------")

def print_list2():
    i = 0
    total = ""
    while i < len(toDo):
        #took away the print statement
        total = total + time[i] + " " + toDo[i] +"\n"
        i+=1
    return total


def week_schedule():
    print("Sunday | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday")
    print("_______|________|_________|___________|__________|________|_________")
    print("       |        |         |           |          |        |         ")
    print("       |        |         |           |          |        |         ")
    print("       |        |         |           |          |        |         ")
    print("       |        |         |           |          |        |         ")
    print("       |        |         |           |          |        |         ")
    print("       |        |         |           |          |        |         ")

def week2():

    table = [["Sunday    ", "Monday   ", "Tuesday   ", "Wednesday   ", "Thursday   ", "Friday   ", "Saturday   "],
            ["         ","           ","           ", "           ", "             ", "         ", "          "],
            ["         ", "           ", "           ", "           ", "            ", "         ", "          "],
            ["         ", "           ", "           ", "           ", "            ", "         ", "          "],
            ["         ", "           ", "           ", "           ", "            ", "         ", "          "],
            ["         ", "           ", "           ", "           ", "            ", "         ", "          "],
            ["         ", "           ", "           ", "           ", "            ", "         ", "          "]
            ]


    this_day = input("Please Enter a day of the week: ")

    if this_day == "Sunday":
        table[2][0] = print_list2()
        for j in range(7):
            for i in table:
                print(i[j], end=" \n")
    elif this_day == "Monday":
        table[2][1] = print_list2()
        for j in range(7):
            for i in table:
                print(i[j], end=" \n")
    elif this_day == "Tuesday":
        table[2][2] = print_list2()
        for j in range(7):
            for i in table:
                print(i[j], end=" \n")
    elif this_day == "Wednesday":
        table[2][3] = print_list2()
        for j in range(7):
            for i in table:
                print(i[j], end=" \n")
    elif this_day == "Thursday":
        table[2][4] = print_list2()
        for j in range(7):
            for i in table:
                print(i[j], end=" \n")
    elif this_day == "Friday":
        table[1][5] = print_list2()
        for j in range(7):
            for i in table:
                print(i[j], end=" \n")
    elif this_day == "Saturday":
        table[2][6] = print_list2()
        for j in range(7):
            for i in table:
                print(i[j], end=" \n")
    else:
        print("Sorry, that's not an option")



main()
add()
week_schedule()
week2()