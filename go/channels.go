package main

import (
	"fmt"
)

//an example that takes in a channel than pass integers
func sum (a []int, c chan int) {
	sum := 0
	//interate over an array of integers, compute its sum, and send the result back into a channel.
	for _, v := range a {
		sum += v
	}
	// send the sum back via the channel c; the arrow conventionally indicates the direction of the data flowing into the channel
	c <- sum
}
//another examples in which we explicity close channles. Only sender can close a channel. 
// take in a unbounded channel. After computing close the channel explicity.
func fibonacci(n int, c chan int) {
	x, y := 0, 1
	for i := 0; i < n; i++ {
		//send it down the channel
		c <- x
		x, y = y, x+y
	}
	//when you done computing close the channel.
	close(c)
}

func main() {
	a := []int {7, 2, 8, -9, 4, 0}
	// create or "make" a channel, just like you would make an array.
	// channels can be unbounded, hence no capacity is required.
	c := make(chan int)
	// let's create a bunfferd channel, with a capacity. Note that buffered channels block when they are full
	// or when they are empty, while assinging or receiving respectively.
	ch := make (chan int, 2)
	ch <- 1
	ch <- 2
	fmt.Println(<-ch)
	fmt.Println(<-ch)
	ch <- 3
	ch <- 4
	fmt.Println(<-ch)
	fmt.Println(<-ch)
	//now is the intereting bit; we want to do sums concurrently, so let's div them up into two task
	//and use GO rountines to establish the task. Give first half to the first Go routine, and the second
	//half of the arry to the second Go routine.
	go sum(a[:len(a)/2], c)
	go sum(a[len(a)/2:], c)
	//now recieve from both and sum up the results
	x, y := <- c, <-c
	//and finally print the results
	fmt.Println(x, y, x+y)
	// create a bounded channel
	fch := make(chan int, 10)
	//goroutine with capacity
	fmt.Println("Fibonacci series:")
	go fibonacci(cap(fch), fch)
	// now keep in reading stuff off the channel. 
	//The loop for i := range c receives values from the channel repeatedly until it is closed. 
	for i := range fch {
		fmt.Println(i)
	}
	//
}