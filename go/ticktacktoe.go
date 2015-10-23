package main

import (
	"fmt"
	"strings"
)

func main() {
	// create a tic-tac-toe board
	// need an [][] string
	game := [][]string {
		[]string{"_", "_", "_"},
		[]string{"_", "_", "_"},
		[]string{"_", "_", "_"},
	}
	// assign X & O
	game[0][0] = "X"
	game[2][2] = "O"
	game[2][0] = "X"
	game[1][0] = "O"
	game[0][2] = "X"
	// print the board
	printBoard(game, "Simba", "Nyope")
}
func printBoard(s [][]string, p1 string, p2 string) {
	//pirnt a two dimenioinal array
	fmt.Printf ("Players: %s vs %s\n", p1, p2)
	for i:= 0; i < len(s); i++ {
		fmt.Printf("%s\n", strings.Join(s[i], " "))
	}
}