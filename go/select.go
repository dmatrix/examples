package main

import (
	"fmt"
)

//lets look at the second version of fibonnaci using select statments where it takes two channels
//and multiplexes on them
func select_fibonacci(c, quit chan int) {
	x, y := 0, 1
	for {
		select {
		case c <- x:
			x, y = y, x+y
		case <-quit:
			fmt.Println("quit")
			return
		}
	}
}

func main() {
	//
	//let's try the second fibonacci with select statements
	sch := make(chan int)
	quit := make(chan int)
	//now this is sublte here create an anonymous Goroutine on the fly
	fmt.Println("From Select Fibonacci series:")
	go func() {
		for i := 0; i < 10; i++ {
			fmt.Println(<-sch)
		}
		//done, send 0
		quit <- 0
	}()
	//now call the select_fibonnacii with two channels
	select_fibonacci(sch, quit)
}