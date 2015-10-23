package main

import "fmt"

func main() {
	
	var a [2] string
	a[0] = "Jules"
	a[1] = "Damji"

	fmt.Println(a[0], a[1])
	fmt.Println(a)
	//let's try some slices
	s := []int {2, 3, 5, 7, 11, 13}
	fmt.Println("s ==", s)

	for i :=0; i < len(s); i++ {
		fmt.Printf("s[%d] == %d\n", i, s[i])
	}
	
	// slices very similar to python lists
	fmt.Println(s[1:5])

	s2 := []int{2, 3, 5, 7, 11, 13}
	fmt.Println("s2 ==", s2)
	fmt.Println("s2[1:4] ==", s2[1:4])

	// missing low index implies 0
	fmt.Println("s2[:3] ==", s2[:3])

	// missing high index implies len(s)
	fmt.Println("s2[4:] ==", s2[4:])

	//making slices & capacity
	aa := make([]int, 5)
	printSlice("aa", aa)
	b := make([]int, 0, 5)
	printSlice("b", b)
	c := b[:2]
	printSlice("c", c)
	d := c[2:5]
	printSlice("d", d)
	//explore nil
	var z []int
	fmt.Println(z, len(z), cap(z))
	if z == nil {
		fmt.Println("nil!")
	}

}
func printSlice(s string, x []int) {
	fmt.Printf("%s len=%d cap=%d %v\n", s, len(x), cap(x), x)
}