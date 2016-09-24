
//class taking an iput parameter
class TestSuite(suiteName: String) { def start() {} }

//declared as a subtype of TestSuite so it can invoke its start() without expliciting extending it
trait RandomSeed { self: TestSuite => 
	def randomStart() {
		util.Random.setSeed(System.currentTimeMillis)
		self.start()
	}
}
//define IdSpec as self-typed trait subclass, allowing it run its randomStart()
class IdSpec extends TestSuite ("ID Tests") with RandomSeed {
	def testId() { println (util.Random.nextInt != 1)}
	override def start() { testId() }

	println("Starting...")
	randomStart()
}

