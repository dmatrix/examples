package main

import "fmt"

//define a global structure that holds Vertex coordinates
type Vertex struct {
	Lat, Long float64
}

//dedclare a global map of strings --> Vertexts
//a map strings-->Vertexts, where stirng is a unique key
var m map[string]Vertex

//another way to define this as way of literals, which are similar to struct literals
//for example
var m2 = map[string]Vertex{
	"Bell Labs": Vertex{
		40.68433, -74.39967,
	},
	"Google": Vertex{
		37.42202, -122.08408,
	},
}
//Yet another way to is eliminate the struct name from the literals
// this is much readble, and something we are use to, say in programming lanauges such Python
//
var m3 = map[string]Vertex{
	"Bell Labs": {40.68433, -74.39967},
	"Google":    {37.42202, -122.08408},
	}

var m4 = make(map[string] int)

func main() {
	//allocate or create an instance of map by making it.
	//at his point the map in empty or nil. It grows as we assign values to it
	m = make (map[string]Vertex)
	m["Bell Labs"] = Vertex{
		40.68433, -74.39967,
	}
	fmt.Println(m["Bell Labs"])
	fmt.Println(m2)
	fmt.Println(m3)

	// mutating maps is just as easy as doing it in Python
	// let's look at some examples
	// create or "make" an empty array
	m4["Answer"] = 42
	fmt.Println("The value:", m4["Answer"])
	//now mutate it as you would in any variable
	m4["Answer"] = 48
	fmt.Println("The value:", m4["Answer"])
	//delete the item, check for its existence, using speical GO's syntax, and 
	// print its value. Of course, it's going to be nil
	delete (m4, "Answer")
	fmt.Println("The value:", m4["Answer"])

	v, ok := m["Answer"]
	fmt.Println("The value:", v, "Present?", ok)
}