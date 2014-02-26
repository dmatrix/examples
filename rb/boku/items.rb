require 'set'
module Items
	IMPORTED 			= 'imported'
	NON_EXEMPT_TYPE	= 'NON_EXEMPT'
	EXEMPT_TYPE 		= 'EXEMPT'
	LOCAL 				= 'LOCAL'
	NON_LOCAL			= 'NON_LOCAL'
	EXEMPT_ITEMS = Set.new
	EXEMPT_ITEMS.add("pills") 
	EXEMPT_ITEMS.add("tablets") 
	EXEMPT_ITEMS.add("capsules") 
	EXEMPT_ITEMS.add("book") 
	EXEMPT_ITEMS.add("books") 
	EXEMPT_ITEMS.add("magazine") 
	EXEMPT_ITEMS.add("periodical") 
	EXEMPT_ITEMS.add("newspaper") 
	EXEMPT_ITEMS.add("beans") 
	EXEMPT_ITEMS.add("soup") 
	EXEMPT_ITEMS.add("chocolate") 
	EXEMPT_ITEMS.add("chocolates") 
	EXEMPT_ITEMS.add("cereal") 
	EXEMPT_ITEMS.add("syrup") 
	EXEMPT_ITEMS.add("bandage") 
	EXEMPT_ITEMS.add("bandages") 
	EXEMPT_ITEMS.add("bandaids") 
	EXEMPT_ITEMS.add("duck") 
	EXEMPT_ITEMS.add("meat") 
	EXEMPT_ITEMS.add("bones") 
	EXEMPT_ITEMS.add("meat") 
	EXEMPT_ITEMS.add("ham") 
	EXEMPT_ITEMS.add("sausages") 
	EXEMPT_ITEMS.add("sausage") 
	EXEMPT_ITEMS.add("chiken") 
	EXEMPT_ITEMS.add("lamb") 
	EXEMPT_ITEMS.add("filet") 
	EXEMPT_ITEMS.add("liver") 
	EXEMPT_ITEMS.add("kidneys") 
	EXEMPT_ITEMS.add("jams") 
	EXEMPT_ITEMS.add("butter") 
	EXEMPT_ITEMS.add("bread") 
	EXEMPT_ITEMS.add("pies") 
	EXEMPT_ITEMS.add("pie") 
end