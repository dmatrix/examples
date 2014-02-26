class Payment
	def initialize(amount)
		@amount = amount
		@percent=0.029
		@fee=0.15
	end
	
	def fee_amount
		 round_to((@amount * @percent) + @fee)
	end
	
	def round_to(value)
		v = (value * 10**2).round.to_f / 10**2 
		return v
	end
end
	