package main

//In Go, a type implements an interface, without an explicit declaration. Without intent of explicit 
//declaration, we can decouple (or even use) some already implemented interfaces methods in the packages.
//This file illustrates some examples borrowed from https://softcover.s3-us-west-2.amazonaws.com/38/GoBootcamp/ebooks/GoBootcamp.pdf
//
import (
	"fmt"
	"os"
	"time"
)
// define a Reader interface with one method Read() which returns n, number of bytes read and error, which is part of the package
type Reader interface {
	Read(b []byte) (n int, err error)
}
// like its counter part Read
type Writer interface {
	Write(b []byte) (n int, err error)
}
// An interface with two methods, this defines that combines a Reader & Writer. And we are not going to implment any of these methods since
// they already exits in the package os. We are going to implicity borrow them.
type ReaderWriter interface {
	Reader
	Writer
}
// Another example where we can illustrate the decoupling of our interface and those already defined in io packages with this example of 
// MyError type.
type MyError struct {
	When time.Time
	What string
}
// Implementation of method Error() which has been declared in io package.
func (e *MyError) Error() string {
	return fmt.Sprintf("at %v, %s", e.When, e.What)
}

func run() error {
	return &MyError { 
			time.Now(), "it didn't work", 
		}
}

func main() {
	var w Writer
	w = os.Stdout
	fmt.Fprintf(w, "hello, writer\n")
	// let's try the run function with short if statement
	if err := run(); err != nil {
		fmt.Println(err)
	}
}
