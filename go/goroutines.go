package main

import (
	"fmt"
	"time"
)

func say (s string) {
	for i:=0; i < 10; i++ {
		time.Sleep(1000 * time.Millisecond)
		fmt.Println(s)
	}
}

func main() {
	//this is like a shell command& runs in the background and returns rightway.
	// these two methods or function call are running currently.
	go say("Hello Gopher!")
	say ("Hello Jules")
}