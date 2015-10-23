package main
//small excecrise to play around with maps or dictionary. Count words in a string.

import (
	"fmt"
	"strings"
)

//declare a global array of strings
 var strs = []string {"Get me all the words and its word count in the line. Do ignore case in all the words",
						 "The quick brown fox jumped over the lazy dog.",
						 "I am learning Go!"}

func WordCount(s string) map[string]int {

	var wc = make(map[string] int)
	var tokens []string = strings.Fields(strings.ToLower(s))
	//iterate over the tokens update approriate count if the key exists
	for _, word := range tokens {
		if v, ok := wc[word]; ok {
			v = v + 1
			wc[word] = v
		} else {
			wc[word] = 1
		}
	}
	// return our dictionary or count of words
	return wc
	
}

func main() {
	//iterate over strings, and do a wordcount on each element
	for _, s := range strs {
		fmt.Println(WordCount(s))
	}
	
}
