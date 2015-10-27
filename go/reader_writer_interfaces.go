package main

import (
	"fmt"
	"os"
	)

// declare an interface that can implement the Read method and returns a tuple of integer, error type
// 
type Reader interface {
	Read (b []byte) (n int, err error)
}
// likewise for Writer
type Writer interface {
	Write(b []byte) (n int, err error)
}
//declare another interface ReadWriter that further can implments Reader and Writer form above
//
type ReadWriter interface {
	Reader
	Writer
}

func main() {
	// w is of type Writer interface
	var w Writer
	// since os.Stdout implements writer interface we can make this implicit assignment
	w = os.Stdout
	//and now we can use them in the call called Fprintf
	fmt.Fprintf(w, "Hello Jules The Writer\n")
}