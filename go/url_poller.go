// Copyright 2010 The Go Authors. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.
//
// For readability and self-learning I have added numerous comments that exlicate Go's philosophy:
// "Don't communicate by sharing memory; share memory by communicating."
// This is one example where we have goroutines share data strcutrues by communicating channels,
// along with select and swtich features in Go. -- Jules S. Damji
// you can view the program at https://golang.org/doc/codewalk/sharemem/ along with an comphrehensive commentary 
// of what each components or sections in the progam is meant to do and why. Good reading!
// As an excercise I'm reading the list of URLs from an input file given to the program on the command line.


package main

import (
	"log"
	"net/http"
	"time"
	"bufio"
    "os"
)

const (
	numPollers     = 2                // number of Poller goroutines to launch
	pollInterval   = 60 * time.Second // how often to poll each URL
	statusInterval = 10 * time.Second // how often to log status to stdout
	errTimeout     = 10 * time.Second // back-off timeout on error
)

// some global variables that we want to poll; his could as easily be read from a NoSQL store
// for scalability. For example, if you want to onitor the state of URLs for various customer sites
// this would be large structure read from a Key/Value or NoSQL store.
/*
var urls = []string{
	"http://www.google.com/",
	"http://golang.org/",
	"http://blog.golang.org/",
	"http://mesosphere.com",
	"http://docker.com",
}
*/

// State represents the last-known state of a URL. Since we don't have classes in Go, struct is not dissimilar.
type State struct {
	url    string
	status string
}

// StateMonitor maintains a map that stores the state of the URLs being
// polled, and prints the current state every updateInterval nanoseconds.
// It returns a chan State to which resource state should be sent.
// Note that it uses Go' Generator patter where the StateMonitor returns a receiving channel
// into which other goroutines can write State data structure. That is essentially sharing memory
// by communicating. 
// args: updateInterval is time duration at which point you want to update the state of URL
// return: it returns a channel for receiving data structure State.
func StateMonitor(updateInterval time.Duration) chan<- State {
	// construct a channel of type State
	updates := make(chan State)
	// a hash map that keeps state of ech unique URL
	urlStatus := make(map[string]string)
	//duration or time at which point the funct will update the log the status
	ticker := time.NewTicker(updateInterval)
	//anonymous Go routine that will loop forever and select upon various events a) time interval and 2) data received on the channel
	go func() {
		// loop forever until one of the switch conditions is satisfied
		for {
			select {
			// handle when the ticker time goes off and update the log with current map of url status
			case <-ticker.C:
				logState(urlStatus)
			// there's some state on the channel updates, assign it to s, and use s to update the map
			case s := <-updates:
				urlStatus[s.url] = s.status
			}
		}
	}()
	// returns rightway while the anonmyous function runs in the background as a light thread.
	return updates
}

// logState prints a state map.
func logState(s map[string]string) {
	log.Println("Current state:")
	for k, v := range s {
		log.Printf(" %s %s", k, v)
	}
}

//Resource represents an HTTP URL to be polled by this program.A Resource represents the state of a URL to be 
//polled: the URL itself and the number of errors encountered since the last successful poll. 
//When the program starts, it allocates one Resource for each URL. 
//The main goroutine and the Poller goroutines send the Resources to each other on channels.
type Resource struct {
	url      string
	errCount int
}

// Poll is the method for Resource struct that executes an HTTP HEAD request for url
// and returns the HTTP status string or an error string.
// Poll also logs the message to stderror and returns the error string.
func (r *Resource) Poll() string {
	resp, err := http.Head(r.url)
	if err != nil {
		log.Println("Error", r.url, err)
		r.errCount++
		return err.Error()
	}
	r.errCount = 0
	return resp.Status
}

// Sleep is another method of the Resource type.
// It sleeps for an appropriate interval (dependent on error state)
// before sending the Resource to done. 
// The channel it recevies as its argument is assgiend the value of r
func (r *Resource) Sleep(done chan<- *Resource) {
	time.Sleep(pollInterval + errTimeout*time.Duration(r.errCount))
	done <- r
}
// The crucial method that handles communicaiton between goroutines using channels and sharing
// pointers to Ressoruces. By sending pointers to Resources on the channels, the senders and reciveers are 
// relinquishing ownership, and as such, we don't need to worry about exclusivity or locking of shared resources.
// In other words, no two goroutines will be accessing the samee pointer to Resource. By this convention, locking in
// Go is uncessary. This supports the notion that in Go you don't commnuicate by sharing memory, you share memory by
// communicating.
// Lastly, in sends Resource to the StateMonitor of the result of the Poll on the resource. As you can see few lines of 
// code acccomplishing a lot, without locking, synchronous blocks, semaphores or condition variables.
// Args: in is receiving channel with pointer to Resource. Note the sytnax in <- channel *Resource
// Args: out is channle to which pointer is assigned. Note the syntax out chan <- *Resource is assigned. This is an 
// indication to suggest to out channel that it's done with the resource.
// Args status, like the first arggument, receives the value of State
func Poller(in <-chan *Resource, out chan<- *Resource, status chan<- State) {
	for r := range in {
		s := r.Poll()
		status <- State{r.url, s}
		out <- r
	}
}
//The main function starts the Poller and StateMonitor goroutines and then loops passing completed Resources back to the pending channel after appropriate delays.
func main() {
	// read a bunch of urls from the file "urls.txt" from the current directory
	// create a data slice of []strings
	urls := []string{}
    file, err := os.Open("urls.txt")
    if err != nil {
        log.Fatal(err)
    }
    defer file.Close()

    scanner := bufio.NewScanner(file)
    for scanner.Scan() {
        line := scanner.Text()
        urls = append(urls, line)
    }
	// Create our input and output channels, with *Resoruces. Pending is for Poll to check status, while complete, as the name suggest is you're done with the Resource
	// The channels provide the means of communication between the main, Poller, and StateMonitor goroutines.
	pending, complete := make(chan *Resource), make(chan *Resource)
	// Launch the StateMonitor, with its goroutine, and its return value of status channel is saved and used in the Poller
	// the argument to StateMonitor is the constant time interval defined above.
	status := StateMonitor(statusInterval)
	// Launch some Poller goroutines. Ths is the where the Pollers do their job. It passes the necessary channels it
	// needs for sharing *Resouce. Note again, share memory by communicaitng. 
	for i := 0; i < numPollers; i++ {
		go Poller(pending, complete, status)
	}

	// Send some Resources to the pending queue.
	go func() {
		for _, url := range urls {
			pending <- &Resource{url: url}
		}
	}()

	for r := range complete {
		go r.Sleep(pending)
	}
}