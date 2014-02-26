class Change

 	attr_accessor :denominations
	
	def initialize (amount)
		@amount=amount
		@denominations = [10.00, 5.00, 2.00, 1.00, 0.50, 0.25, 0.10, 0.05, 0.01]
	end
	
	def print_change (ch)
		ch.each { |k, v| 
			val = k * v
			puts  "Note(s) #{k} x #{v} = #{val}" }
		puts "Amount= #{@amount}"
	end
	
	def get_change()
		amt = @amount
		change_hash = {}
		@denominations.each {|d| 
			#puts "processing denomination #{d}"
			c = amt.div d
			#puts "c = #{c}"
			if c > 0
				change_hash[d] = c
				amt = amt - (c*d)
				#puts "amt = #{amt}"
				amt = round_to(amt)
				#puts "after rounding:#{amt}"
			elsif (amt < d)
				next
			elsif (amt == d) 
				change_hash[d] = 1
			end 
		}
		return change_hash
	end
	
	def do_it
		ch = get_change
		print_change(ch)
	end
	
	def round_to(value)
		v = (value * 10**2).round.to_f / 10**2 
		return v
	end
end