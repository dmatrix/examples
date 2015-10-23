package main

import "fmt"

//global structure with two integer points
type Vertex struct {
		X, Y int
}

//use Struct literals & using struct block
	var (
		v1 = Vertex{1, 2}		//assign type vertex to v1
		v2 = Vertex{X: 1}		//assing type vertex to v2 with X=1, Y=0
		v3 = Vertex{}			// assign an empty vertex to v3
		p = &Vertex{1, 2}		// assign p as a pointer to V
		q = &v1					// assign q as pointer to v1
	)

func main() {
	//use struct assignments
	fmt.Println( Vertex {1, 2})
	v := Vertex {1,2}
	v.X = 4
	fmt.Println(v.X)
	// try pointer referencing
	p:= &v
	p.X = 1e9
	fmt.Println(v)

	fmt.Println( v1, p, q, v2, v3)
}