object CollectionsMapDemo {
	/**
	 * define few types of Maps as global variables to this Singleton
	 * Use the Map[.methods to illusrate the functional nature of Scala programming.
	 * Note the methods like map(), filter(), foreach() and other function values are good illustrations
	 * of how Spark Core API uses similar methods on RDDs. Learning Scala this way aids the learning Spark's 
	 * Scala or Python API
	 */
	val author = "Jules S. Damji"
	val what   = "Learning Scala!"

	/**
	 * Method to print a message Unit is equivalent to void in Java and signals Scala that this is a proceddure
	 * function return a value.
	 */
	 def myPrint(message: String) : Unit = {
	 	val luv = "...And Luving it!"
	 	print (message)
	 	println (author + " is " + what + luv)
	 }

	
	def main(args: Array[String]) {
		// create some  maps
		val i = 0
		var A :Map[Int, String] = Map()
		for (i <-1 to 10) {
			A += (i -> i.toString)
		}
		myPrint("Hello World! ")
		println( "Keys in A : " + A.keys )
      	println( "Values in A : " + A.values )
      	/*two ways to do foreach, define an anonymous function in=>line, like Lambda or with a case
      	 * statement using in-line function 
      	 */
		A.keys.foreach { i =>  
						print("Key = " + i ) 
						println(" Value = " + A(i) )}
		//using case statement
        A.foreach { case (key, value)  => println (key + "-->" + value)}
        //yet another way, using a for statement
        for ((k, v) <- A) (printf("key: %s, value: %s\n", k, v))

        println (A.toSeq)
        println(A.toString)
        // Yet another way to iterate over keys or values
        // Use foreach to print each key.
		A.keys.foreach(print(_))
		println()
		// Use for-loop to iterate over all values.
		for (value <- A.values) {
    		print(value + " ")
		}
		println()
	}
}