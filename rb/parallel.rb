
require 'thread'
require 'open3'

module TomUtil
   # Runs the block for each item in list
   def parallel_each(list, &block)
      threads = []

      list.each do |item|
         threads << Thread.new do
            if block_given? then yield(item); end
         end
      end

      threads.each do |thread|
         begin
            thread.join
         rescue
         end
      end
   end
  
   # Lets you prepend a string to the output lines
   # of the exec.
   def prepend_exec(str, cmd)
      Open3.popen3(cmd) do |stdin, stdout, stderr|
         while true
            begin
               puts "#{str}#{stdout.readline}"
            rescue
               break
            end
         end
      end
   end
end

# vim: filetype=ruby tabstop=3 expandtab 
