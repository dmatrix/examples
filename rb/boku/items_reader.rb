class BasketItems
	
	def initialize(name, items)
		@name=name
		@items=items
	end
	
	def get_name
		"public"
		return @name
	end
	
	def get_basket_items
		"public"
		return @items
	end
	
	def to_string
		"public"
		puts @name
		items.each {|i| puts i}
	end
end

class ItemsBasketReader
	@@basket_filter = "basket-*"
	
	def initialize(source_dir)
		@source_dir=source_dir
		@basket_list = []
		@baskets = Dir[@source_dir + '/' + @@basket_filter]
	end
	
	def read_basket_items(name)
		"private"
		items = []
		IO.foreach(name) { |i| items.push(i.chop) }
		@basket_list.push(BasketItems.new(name, items))
	end
	
	def read_baskets
		"public"
		@baskets.each {|b| read_basket_items(b) }
	end
	
	def get_items
		"public"
		return @basket_list
	end
end