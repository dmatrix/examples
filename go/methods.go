package main
//This is a good organziation for a Go file with methods defined for types. Since Go is not a Object Oriented Programming (OOP)
// it does not have the concpe of classes. Nor does it have the notion of inheritance. Instead, you can defined your own types
// and define methods that become receivers or methods that part of the instance of that typed object. See examples below of Vertex,
// MyFloat, User and UserLocation. Additionally, you use aliases to extend your own types. See Error or MyString, for example.
// 
import (
	"fmt"
	"math"
	"time"
	"strings"
)

//declare type struct in this file. Consider this as a class to which we will define some methods
//a Vertex struct with two coordinates
//
type Vertex struct {
	X, Y float64
}
//methods can be assigned to any type your package. 
type MyInt int
//let's define a method for it, say cube it 
// define a method for this type Vertex
// the method name is Abs() and it returns float64, while receving a pointer to type type struct Vertex
func (v *Vertex) Abs() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
}

//let's define a method for it, say cube it 
func (x MyInt) cube() int {
	return int(x * x * x)
}
//define at struct for Error
type MyError struct {
	When time.Time
	What string
}
//define a method for this type.
func (e *MyError) Error() string {
	return fmt.Sprintf("at %v, %s", e.When, e.What)
}
// a function that returns error, which is defined already
func run() error {
	return &MyError{
		time.Now(),
		"it didn't work",
	}
}
// let's define our own type of String and use Alias to extenc it
// 
type MyString  string

// now the funciton is a method on our type MyString but it uses the build in function string.ToUpper()
func (s MyString) Uppercase() string {
	return strings.ToUpper(string(s))
}
// let's look at another more elaborate type structure with associated methods. These methods work only on object
// of they type defined.

//define a data structure User with onther structur embedeed inside. Very typical of complicated data structures
//
type User struct {
	LastName, FirstName string
	Location 	*UserLocation
}

type Coordinates struct {
	Lat, Long int
}
//now let's define UserLocation structure
type UserLocation struct {
	City, Country string
	LatLong		*Coordinates
}

//This function acts like a constructor and returns a pointer to newly created Coordinates
func NewCoordinates(lat int, long int) *Coordinates {
	return &Coordinates {
			Lat:lat,
			Long:long,
	}
}
// this function acts like as constructor and returns a pointer to UserLocation
func NewUserLocation(city string, country string, coor *Coordinates) *UserLocation {
	
	return &UserLocation {
			City: city,
			Country: country,
			LatLong: coor,
	}
}
//func let's define a constructor function for NewUser
func NewUser(fn string, ln string, location *UserLocation) *User {
	return &User{
		LastName: ln,
		FirstName: fn,
		Location: location,
	}
}

// Now it's time to define methods associated with these complext types of data structures
// This metho is assoicated to the object of type User. It takes a pointer to Uesr and returns 
// a greeting string.
func (u *User) Greetings() string {
	return fmt.Sprintf("Dear %s %s", u.FirstName, u.LastName)
}

// A method associated with object of type Coordinates
func (u *Coordinates) Print() string {
	return fmt.Sprintf("latitude: %d; longitude: %d", u.Lat, u.Long)
}
// A method assocaited with object of type UserLocation
func (u *UserLocation) Print() string {
	return fmt.Sprintf("city: %s; country:%s; coordinates:%s", u.City, u.Country, u.LatLong.Print())
}

// A method associated with the User to print
func (u *User) Print() string {
	return fmt.Sprintf("lastName:%s; FirstName:%s; UserLocation:%s", u.FirstName, u.LastName, u.Location.Print())
}
 
//let's see how they all work
func main() {
	//assisgn vertices to Vertex and then let v point to the address of Vertext (pointer)
	v := &Vertex{3, 4}
	// use v and inovke its method, just as you would if it were a class or object with methods.
	fmt.Println(v.Abs())
	//defining an instance of your type MyInt, with value integer 5
	x := MyInt(5)

	fmt.Println(x.cube())
	// assignment first, followed by evaluation of the if expression: a short cut.
	if err := run(); err != nil {
		fmt.Println(err)
	}
	// let's try some methods and functions on our complext structs User
	//
	coordinates := NewCoordinates (1, 2)
	location := NewUserLocation("Fremont", "CA", coordinates)
	user := NewUser("Jules", "Damji", location)
	fmt.Println(user.Greetings())
	fmt.Println(user.Print())



}