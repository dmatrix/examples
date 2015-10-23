package main

import (
	"fmt"
	"time"
	"runtime"
)

func main() {
	fmt.Println("Welcome to the playground. This is fun way to learn Go!")
	fmt.Println("The time now is", time.Now())

	sum := 1
	// for withouth the semis is while statement in go
	for sum < 10 {
		sum += sum
	}
	fmt.Println(sum)
	//
	// try the switch statement
	fmt.Printf("Go Runs as ....")
	switch os := runtime.GOOS; os {
		case "darwin": 
			fmt.Println("OS X")
		case "linux":
				fmt.Println("Linux")
		default:
			//free bsd, openbsd
			//plan 9, windows
			fmt.Println("%s", os)

	}
	// another switch statement
	fmt.Println("When's Saturday?")
	today := time.Now().Weekday()
	fmt.Println(today)

	switch time.Saturday {
		case today + 0:
		fmt.Println("Today.")
		case today + 1:
		fmt.Println("Tomorrow.")
		case today + 2:
		fmt.Println("In two days.")
		default:
		fmt.Println("Too far away.")
	}
	//switch without condition
	t := time.Now()
	switch {
		case t.Hour() < 12: 
			fmt.Println("Good Morning")
		case t.Hour() < 17:
			fmt.Println("Good Afternoon")
		default:
			fmt.Println("Good evening.")
	}

}