require 'payment'

describe Payment, "#fee_amount" do
    it "returns 2.9% of $10 + $0.15 ($0.44)" do
      payment = Payment.new(10.00)
      payment.fee_amount.should == 0.44
    end
    it "returns 2.9% of $42 + $0.15 ($1.37)" do
      payment = Payment.new(42.00)
      payment.fee_amount.should == 1.37
    end
  end