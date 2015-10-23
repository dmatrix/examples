package main

import "fmt"

func main() {
	//poninter excercise
	i, j := 42, 2701

	p := &i 					// point to i
	fmt.Println(*p)			// deference p, and pirnt the what p is point to, ie i

	*p = 21 				// change whatever p's point to. Now is = 21
	fmt.Println(i)			// print the value if i

	p = &j					// let p point to memory location of j

	*p = *p / 37			// divide j using pointer deferencing
	fmt.Println(j)			// set the new value of j
}