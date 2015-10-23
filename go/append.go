package main

import "fmt"

func main() {
	var a []int

	printSlice("a", a)

	//append an item to an empty or nil slice
	a = append(a, 0)
	printSlice("a", a)
	//append more items, and the slice grows
	a = append(a, 1)
	printSlice("a", a)
	a = append(a, 2, 3, 4)
	printSlice("a", a)

	//let try a range over a slice
	var pow = []int { 1, 2, 4, 8, 32, 64, 128}
	for i, v := range pow {
		fmt.Printf ("2**%d = %d\n", i, v)
	}
}

func printSlice(s string, x []int) {
	fmt.Printf("%s len=%d cap=%d %v\n", s, len(x), cap(x), x)
}