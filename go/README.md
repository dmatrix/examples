##A Gopher Joins the Kingdom of Languages, with Ums, Ahs, Uhh...
A set [learning exercises for GO.](http://tour.golang.org/) is an excellent way to interactively and quickly learn GO. It reminds me, delightfully and ruefully of Python and Scala, mixed with C, but with trimmed syntax nuances, absence of tabs as token separator, and enhanced readbility.

Despite some critics, GO has garned a rapid following, particularly among developers who write embedded system software, agents, and distrubuted systems. Its built-in concurrancy patterns lends itself well to writing concurrent programs. Robert Pike, creator of the language and UNIX's legendary co-founder now at Google, notes eloquently that [concurrency != parallelism.](https://www.youtube.com/watch?v=cN_DpYBzKso). He's particulary emphatic about the languages Concurrent Pattenrs.

Yes, it's not JVM based langauge—it compiles to native code. A good comprise between C++ and C, GO's inherent concurrent model and its implementations of many concurrency notions advanced in a must-read-paper, [Commuicating Sequential Processes (CSP),](http://spinroot.com/courses/summer/Papers/hoare_1978.pdf) by C.A.R Hoare lends itself as a better alternative. Because concurrency and multi-threading programming are inherently difficulty, where one has to deal with semaphores, mutexes, condition variables, and memory barriers, the success of CSP in other languages like Occam and Erlang proved an easy way to abstract this difficulty and provide a higher and simpler level of programming support for concurrency. This notion of CSP is embraced in GO through channels. As a result, many large scale distributed sytems today are implemented in GO, including Google's Kubernetes.

As Pike notes, "Don't communicate by sharing memory, share memory by communicating."

You can watch his lectures on [Youtube.](https://www.youtube.com/watch?v=cF1zJYkBW4A) 

And if you're thinking of writing distributed systems, where concurrency is inherent in your tasks, think about GO. Think about its Concurrency Patterns, and think about its efficiency and brevity of language.

Just like any new programming language, an interactive and quick way to start is by doing it. So get started, and [GO!] (https://blog.golang.org/playground)

Jules S. Damji
