package main.scala
import java.util.Date
/**
 *
 * Object in scala has a state and behaviors. In this case, behavior are functions, while 
 * instance variables are states. Here I've two states represented by instance variables 
 * author and what, and a behavior called myPrint()
 */
object FunctionsDemo {
	/** this is my first scala program
	 * Instance variables or also known as Field variables. Since they're defined
	 * 'val,' they're immutable.
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

	 /**
	 * Let's define a function to demostrate call-by-name concept, central to Scala's functional programming
	 * feature
	 */

	 def time() = {
      println("Getting time in nano seconds")
      System.nanoTime
   	}

   	def delayed( t: => Long ) = {
      println("In delayed method")
      println("Param: " + t)
      println(t)
   	}

   	/**
   	* Another function to demonstrate call-by-variable number of arguments. Here the String* indicates 
   	* that it's an array of String[]
   	*/
   	def printMyArgs(args:String*): Unit = {
   		var i : Int = 0;
   		for (arg <- args) {
   			println("Arg value[" + i + "] = " + arg)
   		
   		}
   	}

   	/**
   	 * This function illusrates how to use partially applied functions. This function will be invoked
   	 * in the main with a special sytnax where only one argumement bound with a constant value while 
   	 * the other evaluated with an new one. Use this strategy where you want to invoke a function multiple times
   	 * with at least one parameter argument constant, whereas the others will be supplied during its invocation.
   	 * See how this is done in the main program.
   	 */

   	 def log(date: Date, message: String)  = {
     println(date + "----" + message)
   }
   	/**
   	* Function to demonstrate default-value parameters
   	*/
   	def sumOfTwo(arg1: Int = 1, arg2: Int = 2): Int = {
   		var sum : Int = 0
   		sum = arg1 + arg2
   		return sum
   	}
	 /**
	 * Functions are at the heart of Scala, hence it's value as a functional programming language than procedural
	 * language. 
	 */
	 def computeSquare(n: Int): Int = {
	 	val sq = n * n
	 	return sq
	 }
	 /**
	 * High-order functions truly demonsrate functinal programming aspects of Scala. These funcitons can
	 * functions as arguments or parameters whose result is a funciton. For example in the following code, apply() function takes another function f and a value v and applies function f to v:
	 */

	 def apply(f: Int => String, v: Int) = f(v)

	 def layout[A](x: A) = "[" + x.toString() + "]"

	 /**
	 * Main program
	 */
	 def main(args: Array[String]) {
	 	myPrint("Hello World! ")
	 	println(computeSquare(5))
	 	/**
	 	* invoke the call-by-name funciton
	 	*/
	 	delayed(time())
	 	/**
	 	* invoke the call-by-variable number of arguments
	 	*/
	 	printMyArgs("one", "two", "three")
	 	/**
	 	* invoke the default value functions
	 	*/
	 	println(sumOfTwo())
	 	println(sumOfTwo(3, 4))
	 	/*
	 	 * let's use log() but do so with one constant value and other different values
	 	 * Note the synax how use bind one of the parameters. The "_" implies this argument 
	 	 * will be given a new value, whereas data will be constant
	 	 */
	 	 val date = new Date
	 	 val logWithDateBound = log(date, _ : String)
	 	 /*
	 	  * Now use logWithDataBound() as the function call, provding only the new argument String
	 	  */
	 	  logWithDateBound("First Argument Message")
	 	  Thread.sleep(1000)
	 	  logWithDateBound("Second Argument Message")
	 	  Thread.sleep(1000)
	 	  logWithDateBound("Third Argument Message")
	 	  /*
	 	   * High-order functions
	 	   */
	 	  println(apply (layout, 10))
		 /* 
	 	 * Now we come to anonymous functions: called functional literals, they are evaluated and instantiated at runtime as 
	 	 * objects called function values. They are synonymous to Python's lambda expressions, known in Scala as closures
	 	 */
		 // inc is a variable which is a function literal and increment the value of x at run time
		 val inc = (x: Int) => x + 1
		 // mult is a variable which is a function literal and multiplies two values: x * y
		 val mult = (x: Int, y: Int) => x * y
		 // it's also possible to define a function literal without arguments
		 val userDir = () => {
			 System.getProperty("user.dir")
		 }

	 	 //invocation of these functional literals
	 	 println {
			 inc(5)
		 }
		 println {
			 mult(5, 5)
		 }
		 println {
			 userDir
		 }
	 }
}