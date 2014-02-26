
module TomUtil
   ###
   # A simple class for padding strings.  You make a Padder
   # with a list of strings and it figures out what the
   # padding should be given any string.  Typical usage is:
   #   padder = Padder.new(list)
   #   padded_str = padder.pad("string from list")
   # or
   #   Padder.pad_list(list).each { |str| puts str + ":" }
   #
   class Padder
      def initialize(list)
         @max_len = list.map { |str| str.length }.max
      end
      
      def pad(str)
         Padder.padded_str(str, @max_len)
      end
      
      def Padder.pad_list(list)
         max_len = list.map { |str| str.length }.max
         list.map { |str| padded_str(str, max_len) }
      end
      
      def Padder.padded_str(str, pad_to)
         if str.length > pad_to then return str; end
         str + (" " * (pad_to - str.length))
      end
   end
end

# vim: filetype=ruby tabstop=3 expandtab 
