package main

import (
	"fmt"
	"time"
)

//declare a struct with two fields
type fakeResult struct {
	body string
	urls []string
}
// declare a mape fakeFetcher is Fetcher that returns canned results.
// this is map with key as url string and the value a struct fakeResult
// For example, url-> struct {string, []string}
type fakeFetcher map[string]*fakeResult

//declare an interface called Fetcher and declare a method called Fetch to implement
type Fetcher interface {
	// for a given url,
	// Fetch returns the body of URL and
	// a slice of URLs found on that page.
	Fetch(url string) (body string, urls []string, err error)
}
//some fake URLs fetcher is a populated fakeFetcher.
var fetcher = fakeFetcher{
	"http://golang.org/": &fakeResult{
		"The Go Programming Language",
		[]string{
			"http://golang.org/pkg/",
			"http://golang.org/cmd/",
		},
	},
	"http://golang.org/pkg/": &fakeResult{
		"Packages",
		[]string{
			"http://golang.org/",
			"http://golang.org/cmd/",
			"http://golang.org/pkg/fmt/",
			"http://golang.org/pkg/os/",
		},
	},
	"http://golang.org/pkg/fmt/": &fakeResult{
		"Package fmt",
		[]string{
			"http://golang.org/",
			"http://golang.org/pkg/",
		},
	},
	"http://golang.org/pkg/os/": &fakeResult{
		"Package os",
		[]string{
			"http://golang.org/",
			"http://golang.org/pkg/",
		},
	},
}
// define a method for type fakeFetcher
func (f fakeFetcher) Fetch(url string) (string, []string, error) {
	if res, ok := f[url]; ok {
		return res.body, res.urls, nil
	}
	return "", nil, fmt.Errorf("not found: %s", url)
}

// global map that keeps track of visited URL
var visited map[string] bool

// define a function that fetches the body of URLS for the incoming url from the fakeFetcher data structure
// send the string[] urls down the channel to the invoker
//
func CrawlEachURLFound(url string, fetcher Fetcher, ch chan []string) {
	body, urls, err := fetcher.Fetch(url)
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Printf("Found: %s %q\n", url, body)
	}
	ch <- urls
}

// Crawl uses fetcher to recursively use goroutine CrawlEachURLFound
// pages starting with url, to a maximum of depth.
func Crawl(url string, depth int, fetcher Fetcher) {
	chURLs := make(chan []string)
	//create a coroutine (or light thread)
	go CrawlEachURLFound(url, fetcher, chURLs)
	visited[url] = true
	for i := 0; i < depth; i++ {
		for {
			select {
			// use select to see if there's any URLs in the channel
			case nextURLs := <- chURLs:
				for _, u := range nextURLs {
					if _, ok := visited[u] ; !ok {
						visited[u] = true
						go CrawlEachURLFound(u, fetcher, chURLs)
					}
				}
			default:
				time.Sleep(50 * time.Millisecond)
				continue
			}
		}
	}
	return
}

func main() {
	//create the global map for visited or seen URLs
	visited = make(map[string] bool)
	Crawl("http://golang.org/", 4, fetcher)
}

