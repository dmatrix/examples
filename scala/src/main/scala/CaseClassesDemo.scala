package main.scala
object CaseClassesDemo {
	/**
	 * This short example shows how case classes can be used in pattern matching, a feature as popular in use as function values and 
	 * closures in Scala. You can define an empyt case class as possible means of match an equivalne object of that type, with writing
	 * you own hashCode, toString or isEqual; the compiler will automatically generate them for you.
	 * Also, note the case class definition is short, where modifiers like "var" or "val" are infered and defaulted to "val," unless
	 * explicitly stated.
	 */
	val author = "Jules S. Damji"
	val what   = "Learning Scala!"

	//case class, empty one. It is used in the patten matching for other objects defined of the same type.
   	case class Person(name: String, age: Int)

   	//Another useful feature in Scala is the pattern matching block that can be use with any type. We use here to
   	// try both case class for pattern match the explict "match" block
   	def matchPerson(person: Any) {
   		person match {
   			case Person("Martin Odersky", 42) => println("Hello Martin, Luving Scala!")
   			case Person("James Gosling", 42)  => println("Hello James, Getting the feel for Java!")
				case Person("Bob Pike", 42) 	  => println("Hello Bob, Using Communication to share memory, not memory to communicate!")
				case Person("Dennis Ritchie", 42) => println("RIP, Dennis. Pointers Rock the World!")
				case Person("Guido Van Rossum", 42) => println("Guido, Love the Pytbon Shell, minus tabs!")
				case Person(name, age) => println("Age: " + age + " year, name: " + name + "what's your claim to fame?!")
				case _ => println("Uhh!..")
		}
	}
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
		val scala = new Person("Martin Odersky", 42)
		val java = new Person("James Gosling", 42)
		val go = new Person("Bob Pike", 42)
		val c  = new Person ("Dennis Ritchie", 42)
		val python = new Person ("Guido Van Rossum", 42)

		myPrint("Hello World! ")
		
		// now use the explicit case and match statement block
		println("------- explicit match with case class-------")
		for (founder <- List(scala, java, go, c, python)) {
			founder match {
				case Person("Martin Odersky", 42) => println("Hello Martin, Luving Scala!")
				case Person("James Gosling", 42)  => println("Hello James, Getting the feel for Java!")
				case Person("Bob Pike", 42) 	  => println("Hello Bob, Using Communication to share memory, not memory to communicate!")
				case Person("Dennis Ritchie", 42) => println("RIP, Dennis. Pointers Rock the World!")
				case Person("Guido Van Rossum", 42) => println("Guido, Love the Pytbon Shell, minus tabs!")
				case Person(name, age) => println("Age: " + age + " year, name: " + name + "what's your claim to fame?!")
			}
		}
		// now let's use the explict match test. Both results should be identical
		//
		println("------- explicit match witch match block -------")
		for (founder <- List(scala, java, go, c, python)) {
			println(matchPerson(founder))
      	}
	
	}
}