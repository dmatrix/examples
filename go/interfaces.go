package main

import (
	"fmt"
	"math"
)


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

func (f MyFloat) Abs() float64 {
	if f < 0 {
		return float64(-f)
	}
	return float64(f)
}


func (v Vertex) Abs() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
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

}