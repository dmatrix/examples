package main.scala
object CollectionsListDemo {
	/**
	 * define few types of lists as global variables to this Singleton
	 */
	var fruits : List[String] = List ("apples", "oranges", "grapes", "mangoes")
	var fruits2 : List[String] = List("tangerine", "apples", "bananas")
	var numbers: List[Int]	   = List(1, 2, 3, 4, 5)
	var numbers2: List[Int] = List(5, 6, 7)
	 //  Empty List.
	var empty: List[Nothing] = List()

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
		// lets apply some foreach function 
		numbers.foreach(func)
		fruits2.foreach(funcLen)
		println(empty.isEmpty)

	}

}