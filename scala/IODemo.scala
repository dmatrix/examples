import java.io.FileReader
import java.io.FileNotFoundException
import java.io.IOException
import scala.io.Source

object IODemo {
	/** Some cases of using I/O and Exception handling in Scala
	 *
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
	 		Source.fromFile(args(0)).getLines().toList.foreach {insertWordIntoMap }
	 		//for ((k, v) <- dict) (printf("KEY: %s, VALUE: %s\n", k, v))
	 		for ((k, v) <- dict) println( new Tuple2(k, v.length))
        } catch {
          case ex: FileNotFoundException => { println("Missing file exception") }
          case ex: IOException => { println("IO Exception") }
        }
    }	
}

