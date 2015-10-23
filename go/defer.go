package main

import "fmt"

func main() {
	//defer this funciton execution after the surronding funciton is returns
	defer fmt.Println("world")

	fmt.Println("hello")
	//deferend statetments put onto a stack, and executed as last-in-first-out

	fmt.Println("Counting...")

	for i:=0; i < 10; i++ {
		defer fmt.Println(i)
	}

	fmt.Println("done")
	
}