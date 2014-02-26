
module TomUtil
   # Lets you pass in a string that has some numeric ranges specified.
   # It will create a list of strings with the ranges played out.
   # host-(9..11) will make ['host-09', 'host-10', 'host-11]
   # If you want to force padding, the last argument specifies it.
   def expand_nums(str, pad = -1)
      str =~ /(.*?)(\(\d+.{2,3}\d+\))(.*)/
      range = nil
      parts = [$1, $2, $3].map do |part|
         if part =~ /^\(/ # Starts with paren
            begin
               range = eval(part)
            rescue
               part
            end
         else
            part
         end
      end

      if range
         nums = range.to_a
         if pad == -1
            pad = nums[nums.length - 1].to_s.length
         end
         strs = nums.map {|n| sprintf("%.#{pad}d", n) }
         strs.map do |str|
            result = ""
            parts.each do |part|
               if part.kind_of?(Range)
                  result << str
               else
                  result << part
               end
            end
            result
         end
      else
         [str]
      end
   end
end

# vim: filetype=ruby tabstop=3 expandtab
