package main

// functions can be values, like any variables. Like Python lambdas or Java 8 or Scala, they can be closures.
// Also, they can return more than one value

// Let's look what do I mean by way of some examples
//

import (
	"fmt"
	"math"
)
//this function takes one functional argument with two arguments.
// As its computed value, it returns a float, hence the last return value outside the functinal arguments parameters
// fn is function that takes two float arguments and returns float; compute returns float
func compute(fn func(float64, float64) float64) float64 {
	// this returns fn evaluated with 3, 4 as arguments
	return fn(3, 4)
}

// Now let's examine closures. They are a bit subtle. 
// this is a simple funciton definition that returns a function that takes an int as argument and returns a int, a sum.
// Note the return value is a function who return value as an int. More explanation here: http://tour.golang.org/moretypes/22
// Also, note that each instance of the closure is bound to the variable outside, in this case sum.
// adder() invokes funct with the value passed to its closure.
func adder() func(int) int {
	sum := 0
	return func(x int) int {
		sum += x
		return sum
	}
}

func main() {
	//hypot is a funciton declaration and it retruns square root of two values as float
	hypot := func(x, y float64) float64 {
		return math.Sqrt(x*x + y*y)
	}
	// print the value computed by the funciton hypot
	fmt.Println(hypot(5, 12))
	//send the fucntion hypot to compute and evalue it with values 3, 4, which are supplied in compute when 
	//hypot is evaluated.
	fmt.Println(compute(hypot))
	// send the math power function to compute and and evaluate the power of 3**4
	fmt.Println(compute(math.Pow))
	//Let's try our adder() as closure
	// assing two variables to the function
	//
	pos, neg := adder(), adder()
	//iterate and compute sums for each value passed.
	for i := 0; i < 10; i++ {
		fmt.Println(
			pos(i),
			neg(-2*i),
		)
	}
}