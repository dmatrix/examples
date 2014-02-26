class Address
  attr_accessor :street, :city, :state, :zip
  #
  # constructor
  #
	def initialize
		@street = @city = @state = @zip = ""
	end
	def to_s
		"\t" + @street + "\n" + \
		"\t" + @city + "\n" + \
		"\t" + @state + "," + @zip + "\n"
	end
end

class Person
	attr_accessor :first_name, :email
	attr_accessor :last_name, :address
	def initialize
		@first_name = @last_name = @email = ""
		@address = Address.new
	end
end

class Applicant < Person
	attr_accessor :residence_of
	attr_accessor :citizen_of
	def initialize
		super
		@residence_of = @citizen_of = ""
 end

class AddressBook
	def initialize
		# empty list or array
		@persons = []
	end
	# add a new person
	def add_entry(p)
		@persons += [p]
		@persons = @persons.sort {|a,b| by_name(a,b) }
	end
	# remove a new person
	def remove_entry(p)
		@person.delete(p)
	end
	# sort by name
	def by_name(a,b)
		if a.first_name == b.first_name  
    then
			a.last_name <=> b.last_name
		else
			a.first_name <=> b.first_name
		end
	end
end
