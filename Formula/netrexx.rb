class NetRexx < Formula
  desc "NetRexx (The Rexx version for the Java Virtual Machine)"
  homepage "https://www.netrexx.org"
  url "https://www.netrexx.org/packages/netrexx-4.05-20230416.tar.gz"
  sha256 "7a37c166dc5ccc0e021f1f1ec93172b5d00c20faa9d688fea31fb8e836501aa7"
  license "ICU"

  depends_on "openjdk" => :build
  depends_on "make" => :build
  #conflicts_with "nothing", because: "this reason"

  def install
    ENV.deparallelize
    system "make"
    #cd "./build" do
    #  system "make", "install"
    #end
  end

  test do
    # (testpath/"test").write <<~EOS
    #   #!#{bin}/rexx
    #   Parse Version ver
    #   Say ver
    # EOS
    # chmod 0755, testpath/"test"
    # assert_match version.to_s, shell_output(testpath/"test")
  end
end
