package main

import (
	"fmt"
	"math"
)

// A interface type is defined by the set of methods. It can one or more methods. 
// A value of interface type (or argumen to it) can hold any value that implements those methods.
// Below are a number of interface types, and for each interface type, we deinfe a method. For example,
// MyFloat is an interface type, so is User 

//declare a structure with two floating points
type Vertex struct {
	X, Y float64
}

//like interfaces in Java, GO offers an ability to declare an interface with methods that an return different values
// 
type Abser interface {
	Abs() float64
}

// declaring our on type from previous exercise
//
type MyFloat float64

//defining an interface method Abs() for type MyFloat
func (f MyFloat) Abs() float64 {
	if f < 0 {
		return float64(-f)
	}
	return float64(f)
}

//implementing an interface method Abs() for type Vertex
func (v Vertex) Abs() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
}
//let's define struct User and then define an Interface type for it.
type User struct {
	FirstName, LastName string
}
// define an interace called Namer that must return a string
type Namer interface {
	Name() string
}

// Name() implements this interface method Name for the object User type
func (u *User) Name() string {
	return fmt.Sprintf("%s %s", u.FirstName, u.LastName)
}
// Another generic interface method that takes Namer as in interface type value, and any object that has defined Name() function 
// can be passed to it, and its respective implemented method will be invoked.
// We'll look at an example in the main program
func Greet(n Namer) string {
	return fmt.Sprintf("Dear %s", n.Name())
}

//another type struct for which we going to employ our Name interface
type Customer struct {
	Id int
	FullName string
}

//this funciton implements the iterface method Name()
func (c *Customer) Name() string {
	return c.FullName
}

func main() {
	// a now is a type interface that has Abs() at its methond, returning float64
	var a Abser
	f := MyFloat(-math.Sqrt2)
	v := Vertex{3, 4}

	a = f  // a MyFloat implements Abser
	a = &v // a *Vertex implements Abser
	// In the following line, v is a Vertex (not *Vertex)
	// and does NOT implement Abser.
	a = v
	fmt.Println(a.Abs())
	//some examples of using Namer interface
	u := &User{"Jules", "Damji"}
	// now invoke the Name() defined the interface of the object user
	// here the Name() function associated with User object will be invoked.
	fmt.Println(Greet(u))
	//another example where we can use the Greet().
	c := &Customer {42, "Arthur Dent"}
	fmt.Println(Greet(c))

}