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

// Yet another type
type Person struct {
	Name string
	City string
	Age  int
}

// define a Stringer method for person
//
func (p Person) String() string {
	return fmt.Sprintf("%v, %v, (%v years)", p.Name, p.City, p.Age)
}
// another Stringer method for type IPAddr
type IPAddr [4]byte

// TODO: Add a "String() string" method to IPAddr.
func (ip IPAddr) String() string {
	return fmt.Sprintf("[%d.%d.%d.%d]",ip[0],ip[1],ip[2],ip[3])
}

func main() {
	// w is of type Writer interface
	var w Writer
	// since os.Stdout implements writer interface we can make this implicit assignment
	w = os.Stdout
	//and now we can use them in the call called Fprintf
	fmt.Fprintf(w, "Hello Jules The Writer\n")
	//let's try the Person struct and use its methods
	j := Person{ "Jules S. Damji", "Fremont", 1000}
	a := Person{"Arthur Dent", "Milkyway Galaxy", 42}
	z := Person{"Zaphod Beeblbrox", "Andromeda", 9001}

	//now use fmt.println and Person's methods
	fmt.Println(j, a, z)
	fmt.Println()
	fmt.Println(j.String(), a.String(), z.String())
	//let's try the IPAddr Stringer method
	addrs := map[string]IPAddr{
		"loopback":  {127, 0, 0, 1},
		"googleDNS": {8, 8, 8, 8},
	}
	for n, v := range addrs {
		fmt.Printf("%v: %v\n", n, v)
		fmt.Println(v.String())
		fmt.Println()
		fmt.Println(v.String())
	}
}