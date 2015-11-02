package main

// You have 50 bitcoins to distribute to 10 users: Matthew, Sarah, Augustus,
// Heidi, Emilie, Peter, Giana, Adriano, Aaron, and Elizabeth. The coins will be distributed
// based on the vowels contained in each name where:
// a: 1 coin e: 1 coin i: 2 coins o: 3 coins u: 4 coins
// and a user can’t get more than 10 coins. Print a map with each user’s name
// and the amount of coins distributed. After distributing all the coins, you should
// have 2 coins left.

// The output should look something like that:
// map[Matthew:2 Peter:2 Giana:4 Adriano:7 Elizabeth:5 Sarah:2 Augustus:10 Heidi:5 Emilie:6 Aaron:5]
// Coins left: 2

import "fmt"

var (
		coins = 50
		users = []string {
			"Matthew", "Sarah", "Augustus", "Heidi", "Emilie",
			"Peter", "Giana", "Adriano", "Aaron", "Elizabeth",
		}
		distribution = make(map[string]int, len(users))
	)

// given a name return the number of coins based on number of vowels conatined in the name.
// the number of coins is governed by the following rule
// a: 1 coin e: 1 coin i: 2 coins o: 3 coins u: 4 coins
func coinsForUser(name string) int {
	n := len(name)
	total := 0
	for i:=0; i < n; i++ {
		switch name[i] {
			case 'A', 'a':
				total++
			case 'E', 'e':
				total++
			case 'I', 'i':
				total = total + 2
			case 'O', 'o':
				total = total + 3
			case 'U', 'u':
				total = total + 4
		}
	}
	return total
}
func main() {
	for _, name := range users {
		if (coins > 2) {
			v := coinsForUser(name)
			// a user can't get more than 10 coins, so set the limit here.
			if (v > 10) {
				v = 10
			}
			distribution[name] = v
			coins = coins-v
		}
	}
	fmt.Println(distribution)
	fmt.Println("Coins left:", coins)
}