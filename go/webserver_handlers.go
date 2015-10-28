package main
	
import (
	"fmt"
	"log"
	"net/http"
)


//lets define some types and their http handlers
// Hello, first
type Hello struct {}
// implement the type Handler interface in teh http package

func (h Hello) ServeHTTP(
	w http.ResponseWriter,
	r *http.Request) {
		fmt.Fprint(w, "Hello Jules, Gopher is way cool & easy!")
}

//define the type String and its hanlder
type String string
//define its http handler
func (s String) ServeHTTP (w http.ResponseWriter,r *http.Request) {
		fmt.Fprint(w, s)
}
//define the type Struct and its http handler
type Struct struct {
	Greeting string
    Punct    string
    Who      string
}
//define its http handler
func (st Struct) ServeHTTP (w http.ResponseWriter,r *http.Request) {
	fmt.Fprint(w, st.Greeting, st.Punct, st.Who)
}

func main() {
	// register handles
	http.Handle("/", Hello{})
	http.Handle("/string", String("Welcome, I'm a the Gopher hanlder for String"))
	http.Handle("/struct", &Struct {"Welcome, I'm a the Gopher hanlder for Struct", ":", "Gophers"})
	err := http.ListenAndServe("localhost:4000", nil)
	if err != nil {
		log.Fatal(err)
	}
	
}