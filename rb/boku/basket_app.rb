#!/usr/bin/ruby

require 'optparse'
require 'items_reader'
require 'parse_item'

class BasketApp
	attr_reader:options
	def initialize(arguments)
		@arguments = arguments
	end
	
	def usage
		puts "basket_app.rb basket_directory"
	end
	
	def run()
		if ARGV.length != 1
			usage
			exit 1
		end
		basket_dir = ARGV[0]
		begin 
			items_reader = ItemsBasketReader.new(basket_dir)
			items_reader.read_baskets()
			basket_items = items_reader.get_items()
			basket_items.each do |bi|
				total_tax=0.00
				total_amount=0.00
				puts "Processing Basket:" + bi.get_name
				items=bi.get_basket_items
				items.each do |item|
					begin
						parsed_item = ParseItemFactory.creareParseItem(item)
						price=parsed_item.get_price
						tax = parsed_item.compute_tax()
						subtotal = price + tax
						puts parsed_item.get_item + " " + subtotal.to_s
						total_tax = total_tax + tax
						total_amount = total_amount + subtotal
					rescue Exception => e
						puts e.message
					end
				end
				puts ("Sales Taxes:" + total_tax.to_s)
				puts ("Total:"			+ total_amount.to_s)
			end
		rescue Exception => ex
			puts ex.message
		end
	end
	
end

app=BasketApp.new(ARGV)
app.run()

