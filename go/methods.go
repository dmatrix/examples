package main

import (
	"fmt"
	"math"
	"time"
)

//declare type struct in this file. Consider this as a class to which we will define some methods
//a Vertex struct with two coordinates
//
type Vertex struct {
	X, Y float64
}
//methods can be assigned to any type your package. 
type MyInt int
//let's define a method for it, say cube it 
// define a method for this type. 
// the method name is Abs() and it returns float64, while receving a pointer to type type struct Vertex
func (v *Vertex) Abs() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
}

//let's define a method for it, say cube it 
func (x MyInt) cube() int {
	return int(x * x * x)
}
//define at struct for Error
type MyError struct {
	When time.Time
	What string
}
//define a method for this type.
func (e *MyError) Error() string {
	return fmt.Sprintf("at %v, %s", e.When, e.What)
}
// a function that returns error, which is defined already
func run() error {
	return &MyError{
		time.Now(),
		"it didn't work",
	}
}

//let's see how they work
func main() {
	//assisgn vertices to Vertex and then let v point to the address of Vertext (pointer)
	v := &Vertex{3, 4}
	// use v and inovke its method, just as you would if it were a class or object with methods.
	fmt.Println(v.Abs())
	//defining an instance of your type MyInt, with value integer 5
	x := MyInt(5)

	fmt.Println(x.cube())

	if err := run(); err != nil {
		fmt.Println(err)
	}

}