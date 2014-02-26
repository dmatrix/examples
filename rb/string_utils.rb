require 'set'

module STRING_UTILS
  
  def remove_duplicates(s)
    arr = s.split(//)
    set = Set.new()
    buf=[]
    arr.each do |c|
      if c == ' '
        buf.add(c)
        continue
      end
      if not set.member?(c)
        buf.push(c)
        set.add(c)
      end
    end
    return buf.to_s
  end
  
  def trim_spaces(s)
    arr = s.split(//)
    front = false
    back = false
    fidx = 0
    len = arr.length
    bidx= len-1
    buf=[]
    for i in 0..len-1 do
      if front and back then
        break
      end
      if (not front and arr[i] != ' ') then
        front = true
        fidx=i
      end
      if (not back and arr[bidx-i]  != ' ') then
        back = true
        bidx=bidx - i
      end
    end
    for i in fidx..bidx do
      buf.push(arr[i])
    end
    return buf.to_s
  end
  
  def reverse(s)
    arr = s.split(//)
    len = arr.length
    for i in 0..len/2 do
      c=arr[len-i]
      arr[len-i]=arr[i]
      arr[i] = c
    end
    return arr.to_s
  end
end