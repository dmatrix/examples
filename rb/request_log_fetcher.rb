#! /usr/bin/env ruby
# A script to fetch request filter log files form the ife machines from production. Arguments to the program
# are a text file, use name, which is option and defaults to 'ebrary' and the destination directory where
# the file ought to be copied into.
# Example: request_log_fetcher.rb -h
#Usage: request_log_fetcher [options]
#    -d destination_directory
#    -u user
#    -f machine_file
#   -h
# The format of the machine file is simple.
# <hostname> <space> directory_where_request_files reside
# eg
# ife01.ebrary.com /var/log/ebrary/isis/usage
#ife02.ebrary.com /var/log/ebrary/isis/usage
#...
#ife0N.ebrary.com /var/log/ebrary/isis/usage
#
#
require 'rubygems'
require 'tomutil/parallel'
require 'optparse'
include TomUtil

class RequestLogFetcher
    REQUEST_FILES=%w[search-requests.log, document-requests.log, annotation-requests.log bookshelf-requests.log]
  def initialize (mdir_map, dest_dir, user='ebrary')
    @mdir_map = mdir_map
    @dest_dir = dest_dir
    @user=user
  end
  
  def fetch()
    REQUEST_FILES.each do |file|
      parallel_each(@mdir_map.keys) do |host|
      begin
         log_file=File.join(@mdir_map[host], file)
        prepend_exec(host + ": ", "scp #{@user}@#{host}:#{log_file}  #{@dest_dir} " )
      rescue Exception => e
        puts e.message
        puts "Failed to fetch #{log_file} from host #{host}"
      end
    end
   end
  end
end

OptionParser.new do |o|
  o.on('-d destination_directory') { |d| $directory= d }
  o.on('-u user') { |u| $user= u }
  o.on('-f FILENAME') { |f| $filename = f}
  o.on('-h') { puts o; exit }
  o.parse!
end

if not $filename or not $directory
  then
  puts "usage: request_log_fetcher.rb -h"
  exit
end

hosts_dirs = {}
IO.foreach($filename) do |line|
   tokens = line.chop.split
   if tokens.length == 2
    hosts_dirs[tokens[0]] = tokens[1]
   end
end

log_fetcher = RequestLogFetcher.new(hosts_dirs, $directory, $user)
begin
  log_fetcher.fetch()
rescue Exception => ex
  puts ex.message
end
