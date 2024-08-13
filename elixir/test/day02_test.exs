defmodule Day02Test do
  use ExUnit.Case

  test "part1 with testing data" do
    {:ok, content} = File.read("test/day02_testing.txt")
    assert Day02.part1(content) == 8
  end

  test "part1 with real data" do
    {:ok, content} = File.read("test/day02_real.txt")
    assert Day02.part1(content) == 2776
  end

  test "part2 with testing data" do
    {:ok, content} = File.read("test/day02_testing.txt")
    assert Day02.part2(content) == 2286
  end

  test "part2 with real data" do
    {:ok, content} = File.read("test/day02_real.txt")
    assert Day02.part2(content) == 68638
  end
end
