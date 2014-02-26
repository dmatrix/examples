require 'items'
class ParsedItem
	@@sLocalTax = 10.00;
	@@sImportTax = 5.00;
	
	def initialize
		@description=nil
		@exempt_type=nil
		@import_type=nil
		@num_of_items=0
		@price=0.0
	end
	
	def set_num_of_items(value)
		"public"
		@num_of_items=value
	end
	
	def get_num_of_items
		"public"
		return @num_of_items
	end
	
	def get_import_type
		"public"
		return @import_type
	end
	
	def set_import_type(value)
		"public"
		@import_type=value
	end
	
	def set_price(value)
		"public"
		@price=value
	end
	
	def get_price
		"public"
		return @price
	end
	
	def set_exempt_type(value)
		"public"
		@exempt_type=value
	end
	
	def get_exempt_type
		"public"
		return @exempt_type
	end
	
	def set_description(value)
		"public"
		@description=value
	end
	
	def get_description
		"public"
		return @description
	end
	
	def get_item
		"public"
		idx = @description.rindex("at")
		if idx < 0
			return @description
		end
		return @description[0,idx]
	end
	
	def round(f)
		"private"
		(f*20).round / 20.0
	end
	
	def compute_tax
		"public"
		taxes = 0.0;
		total_tax = 0.0
		total_price = @num_of_items * @price
		if @import_type == Items::NON_LOCAL
			taxes = taxes + @@sImportTax
		end
		if @exempt_type == Items::NON_EXEMPT_TYPE
			taxes = taxes + @@sLocalTax
		end
		total_tax = (total_price * taxes) / 100.00;
		
		return round(total_tax)
	end
	
	def to_string
		puts "Number	=" + @num_of_items.to_s
		puts "Item		=" + @description
		puts "Exempt	=" + @exempt_type
		puts "Import	=" + @exempt_type
		puts "Price		=" + @price.to_f
	end
end

class ParseItemFactory
	def ParseItemFactory.creareParseItem(description)
		"public"
		parse_item = ParsedItem.new()
		parse_item.set_description(description)
		tokens = description.split()
		if tokens.length == 0
			raise "Illegal description type: No items described"
		end
		num = tokens[0].to_i
		if num <= 0
			raise "Illegal number of items in the description"
		end
		parse_item.set_num_of_items(num)
		set = Set.new()
		tokens.each { |t| 
			set.add(t.downcase)
		}
		if set.member?(Items::IMPORTED)
			parse_item.set_import_type(Items::NON_LOCAL)
		else
			parse_item.set_import_type(Items::LOCAL)
		end
		parse_item.set_price(tokens[tokens.length-1].to_f)
		set2 = Set.new(set)
		int_set = set2.intersection(Items::EXEMPT_ITEMS)
		if (not int_set.empty?)
			parse_item.set_exempt_type(Items::EXEMPT_TYPE)
		else
			parse_item.set_exempt_type(Items::NON_EXEMPT_TYPE)
		end
		return parse_item
	end
end