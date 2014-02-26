#!/usr/bin/env ruby

require 'rubygems'

hosts = %w[]
IO.foreach("cluster.txt") {
	|name| hosts.push name.chop
}

hosts.each do |host| 
	print host, " ", host.length
	puts
end