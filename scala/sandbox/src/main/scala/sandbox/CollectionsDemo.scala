package main.scala.sandboxobject

class CollectionsDemo {
	/**
	 * define few types of lists as global variables to this Singleton
	 * Use the List.methods to illusrate the functional nature of Scala programming.
	 * Note the methods like map(), filter(), foreach() and other function values are good illustrations
	 * of how Spark Core API uses similar methods on RDDs. Learning Scala this way aids the learning Spark's 
	 * Scala or Python API
	 */
	val author = "Jules S. Damji"
	val what   = "Learning Scala!"

	var fruits : List[String] = List ("apples", "oranges", "grapes", "mangoes")
	var fruits2 : List[String] = List("tangerine", "apples", "bananas")
	var numbers: List[Int]	   = List(1, 2, 3, 4, 5)
	var numbers2: List[Int] = List(5, 6, 7)
	 //  Empty List.
	var empty: List[Nothing] = List()

	/**
	 * Method to print a message Unit is equivalent to void in Java and signals Scala that this is a proceddure
	 * function return a value.
	 */
	 def myPrint(message: String) : Unit = {
	 	val luv = "...And Luving it!"
	 	print (message)
	 	println (author + " is " + what + luv)
	 }

	def func(elem: Int): Unit = {
		val sq = elem * elem
		val fs = printf("Element = %d; squared = %d", elem, sq)
		println (fs)
	}

	def funcLen(elem: String): Unit = {
		val fs = printf("Element = %s; Length = %s", elem, elem.length)
		println (fs)
	}

	def main(args: Array[String]) {

		myPrint("Hello World! ")
		println(fruits)
		//change fruits
		fruits = fruits.::("kivis")
		// use the mapping function on numbers 
		val incNumbers = numbers.map((x: Int) => x + 1)
		println (incNumbers)
		println(List.concat(fruits, fruits2))
		println( "fruits.:::(fruits2) : " + fruits.:::(fruits2))
		println(numbers)

		// double the numbers using map() on each element in the list
		numbers = numbers.map ( (x: Int) => x + x)

		// use the concanate member
		val allNumbers = List.concat(numbers, numbers2)
		println(allNumbers)

		// use the sum  method
		println(allNumbers.sum)

		// create a filtered list
		val filteredNumbers = allNumbers.filter(_ >= 5).sorted
		println(filteredNumbers)

		//apply the map function to a concanated list of numbers
		val tuples = allNumbers.map( (x: Int) => (x, 1))
		println (tuples)

		//let's apply a foreach method with two finctions to separate lists 
		numbers.foreach(func)
		fruits2.foreach(funcLen)
		//check if list is empty
		println(empty.isEmpty)

	}

}