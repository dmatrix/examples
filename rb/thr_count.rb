#!/usr/bin/ruby
# Short example demonstrating Ruby Threads and Thread Local Variables
#

class Singleton
	@@the_instance = nil
	@@count = 0
	#
	# Static method to return the single instance
	def Singleton.get_instance
		if not @@the_instance
			@@the_instance = Singleton.new
		end
		@@the_instance
	end
	
	def run_threads
		arr=[]
		10.times do |i|
			arr[i] = Thread.new {
				sleep(rand(0)/10.0)
				Thread.current["my_thr_count"] = @@count
				@@count += 1
			}
		end
		arr.each { |t| t.join; print t["my_thr_count"], ", " }
		puts "Total Count = #{@@count}"
	end
end