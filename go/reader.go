package main

import (
	"fmt"
	"io"
	"strings"
)

func main() {
	r := strings.NewReader("Hello, Jules The Reader!")
	//make an array of 8 bytes for reading data
	b := make([]byte, 8)
	//now read the data into it from the variable "r". Cool stuff!
	for {
		//n is the number of bytes read, while err returns nil or error string
		n, err := r.Read(b)
		//print stuff out
		fmt.Printf("n = %v err =%v b = %v\n", n, err, b)
		// now print slice of it 
		fmt.Printf("b[:%v] = %q\n",n, b[:n])
		//check if we reached EOF, since r is reader and complies with its interface
		if err == io.EOF {
			break
		}
	}
}