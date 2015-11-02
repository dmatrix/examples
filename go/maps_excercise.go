package main

import (
	"fmt"
)
//Given a list of names, you need to organize each name within a slice based on
//its length. For example, your impementaition of the new data structure should print out 
// the following:
//[[] [] [Ava Mia] [Evan Neil Adam Matt Emma] [Emily Chloe]
// [Martin Olivia Sophia Alexis] [Katrina Madison Abigail Addison Natalie]
// [Isabella Samantha] [Elizabeth]]
// This is a slice of slice of strings arranged in ascedning order of collection of strings of the same length

var names = []string { "Katrina", "Evan", "Neil", "Adam", "Martin", "Matt",
						"Emma", "Isabella", "Emily", "Madison",
						"Ava", "Olivia", "Sophia", "Abigail",
						"Elizabeth", "Chloe", "Samantha",
						"Addison", "Natalie", "Mia", "Alexis",
					}
var Names map[int][]string

func main() {
	//create a map
	Names = make (map[int][]string)
	for _, s := range names {
		l := len(s)
		v, ok := Names[l]
		if ok {
			// found value so add the name to the slice of strings 
			v = append (v, s)
			Names[l] = v
		} else {
			v = []string{}
			Names[l] = append (v, s)
		}

	}
	fmt.Printf("%v\n", Names)
	// another way to do this without using the map
	// slightly less efficient since it iterates twice over the loop
	// find the length of the longest word in the slice, and that will be the 
	// capacity of the final array.
	var maxLen int
	//iterate over the names, and find the maxLend
	for _, name := range names {
		if l := len(name); l > maxLen {
			maxLen = l
		}
	}
	// create a slice string[][] with capacity maxLen
	output := make([][]string, maxLen)
	for _, name := range names {
		//index into the array and append any names with the same string length
		output[len(name)-1] = append(output[len(name)-1], name)
	}
	fmt.Printf("%v", output)
}