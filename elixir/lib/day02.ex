defmodule GameParser do
  import NimbleParsec

  # Parser for the color (blue, red, green)
  color =
    choice([
      string("blue"),
      string("red"),
      string("green")
    ])
    |> label("color")

  # Parser for the quantity, which is an integer
  quantity =
    integer(min: 1)
    |> label("quantity")

  whitespace = string(" ")
  # Parser for a single "quantity color" pair, e.g., "3 blue"
  quantity_color =
    quantity
    |> ignore(optional(whitespace))
    |> concat(color)
    |> wrap()
    # |> map({fn [quantity, color] -> {String.to_atom(color), quantity} end})
    |> label("quantity_color")

  # Parser for a single round, e.g., "3 blue, 4 red"
  round =
    quantity_color
    |> repeat(ignore(string(",")) |> ignore(optional(whitespace)) |> concat(quantity_color))
    |> wrap()
    |> label("round")

  # Parser for a single game, e.g., "1 blue, 2 green; 3 green, 4 blue, 1 red"
  game =
    ignore(string("Game "))
    |> integer(min: 1)
    |> ignore(string(": "))
    |> concat(round)
    |> repeat(ignore(string("; ")) |> concat(round))
    |> label("game")
    |> wrap()

  # Parser for the entire input, e.g., multiple games
  games =
    game
    |> repeat(ignore(optional(string("\n"))) |> concat(game))
    |> label("games")

  # Public function to parse the input string
  defparsec(:parse_games, games)
end

defmodule Day02 do
  defmodule Game do
    defstruct [:game, :draws]
  end

  defmodule Draw do
    defstruct red: 0, green: 0, blue: 0
  end

  def fillDraws(draws) do
    Enum.reduce(draws, %Draw{}, fn [c, color], acc ->
      Map.update!(acc, String.to_atom(color), fn _ -> c end)
    end)
  end

  def transform([id | draws]) do
    %Game{game: id, draws: Enum.map(draws, &fillDraws(&1))}
  end

  def possible?(draw, bag) do
    draw.red <= bag.red && draw.green <= bag.green && draw.blue <= bag.blue
  end

  def part1(input) do
    {:ok, result, _, _, _, _} = GameParser.parse_games(input)
    bag = %Draw{red: 12, green: 13, blue: 14}

    result =
      Enum.map(result, &transform(&1))
      |> Enum.filter(fn game ->
        Enum.all?(game.draws, fn draw -> possible?(draw, bag) end)
      end)
      |> Enum.map(& &1.game)
      |> Enum.reduce(0, &(&1 + &2))

    result
  end

  def smaller_possible(draws) do
    red = Enum.map(draws, & &1.red) |> Enum.max()
    blue = Enum.map(draws, & &1.blue) |> Enum.max()
    green = Enum.map(draws, & &1.green) |> Enum.max()

    %Draw{red: red, green: green, blue: blue}
  end

  def power(draw) do
    draw.red * draw.blue * draw.green
  end

  def part2(input) do
    {:ok, result, _, _, _, _} = GameParser.parse_games(input)

    Enum.map(result, &transform/1)
    |> Enum.map(& &1.draws)
    |> Enum.map(&smaller_possible/1)
    |> Enum.map(&power/1)
    |> Enum.reduce(0, &(&1 + &2))
  end
end
