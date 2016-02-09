package main.scala
import java.io._
import scala.io.Source

object IODemo {
	/** Some cases of using I/O and Exception handling in Scala. This example uses Map, Tuples, and Iterator's foreach 
	 * methods. The input to the program is the list of words from /usr/share/dict/web2.
	 */
	 val author = "Jules S. Damji"
	 val what   = "Learning Scala!"

	 var dict : Map[Char, List[String]] = Map() 
	 /**
	 * Method to print a message Unit is equivalent to void in Java and signals Scala that this is a proceddure
	 * function return a value.
	 */
	 def myPrint(message: String) : Unit = {
	 	val luv = "...And Luving it!"
	 	print(message)
	 	println(author + " is " + what + luv)
	 }
	 /**
	  * Creae a map for word[0] --> list of words in the dictoary input
	 */
	 def insertWordIntoMap(word: String) : Unit = {
	 	val char = word (0)
	 	var lst: List[String] = List().::(word)
	 	if (dict.contains(char)) {
	 		var value: List[String] = dict(char)
	 		lst = lst.:::(value)
	 		dict += (char->lst)
	 	} else {
	 		dict += (char->lst)
	 	}
	 }

	def main(args: Array[String]) {
		myPrint("Hello World! ")
	 	//read words from the a huge file
	 	if (args.length != 1) {
	 		println ("Usage: IODemo <file>")
	 		System.exit(1)
	 	}
	 	try {
			// read the file, create iterator, convert into a list, apply a fucntion using foreach method
			// of the list
			Source.fromFile(args(0)).getLines().toList.foreach {
				insertWordIntoMap
			}
			val writer = new PrintWriter(new File("IODemo.run.txt"))
			for ((k, v) <- dict) {
				val fs = "KEY: " + k + "VALUE: " + v + "\n"
				writer.write(fs)
			}
			writer.close()
			// for brevity print out the (Key, <number of words>) that begin with the character as the key.
			for ((k, v) <- dict) println(new Tuple2(k, v.length))
		} catch {
          case ex: FileNotFoundException => { println("Missing file exception") }
          case ex: IOException => { println("IO Exception") }
        }
    }	
}

