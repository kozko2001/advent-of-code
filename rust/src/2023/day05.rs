use nom::{
    bytes::complete::tag,
    character::complete::{digit1, line_ending, multispace0, multispace1},
    combinator::{map, map_res},
    multi::separated_list1,
    sequence::{preceded, tuple},
    IResult,
};

#[derive(Debug)]
struct MapRange {
    dest_start: u64,
    source_start: u64,
    length: u64,
}

#[derive(Debug)]
struct Map {
    ranges: Vec<MapRange>,
}

#[derive(Debug)]
struct Almanac {
    seeds: Vec<u64>,
    maps: Vec<Map>,
}

fn main() {
    println!("hello world")
}

fn solve_part1(input: &str) -> u32 {
    let almanac = parse_almanac(input).unwrap();
    dbg!("{:?}", almanac);
    0
}

fn solve_part2(input: &str) -> u32 {
    0
}

fn number(input: &str) -> IResult<&str, u64> {
    map_res(digit1, str::parse)(input)
}

// Parse the seeds line
fn seeds(input: &str) -> IResult<&str, Vec<u64>> {
    preceded(tag("seeds: "), separated_list1(multispace1, number))(input)
}

fn map_range(input: &str) -> IResult<&str, MapRange> {
    map(
        tuple((
            number,
            preceded(multispace1, number),
            preceded(multispace1, number),
        )),
        |(dest_start, source_start, length)| MapRange {
            dest_start,
            source_start,
            length,
        },
    )(input)
}

// Parse a complete map section
fn map_section(input: &str) -> IResult<&str, Map> {
    // Skip the map header line (e.g., "seed-to-soil map:")
    let (input, _) = tuple((
        multispace0,
        nom::character::complete::not_line_ending,
        line_ending,
    ))(input)?;

    // Parse the ranges
    let (input, ranges) = separated_list1(line_ending, map_range)(input)?;

    Ok((input, Map { ranges }))
}

fn parse_almanac(input: &str) -> IResult<&str, Almanac> {
    let (input, seeds) = seeds(input)?;
    let (input, _) = multispace1(input)?;
    let (input, maps) = separated_list1(multispace1, map_section)(input)?;

    Ok((input, Almanac { seeds, maps }))
}

#[cfg(test)]
mod tests {
    use super::*;

    const TEST_INPUT: &str = "seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4";

    #[test]
    fn test_part1() {
        let result = solve_part1(TEST_INPUT);
        assert_eq!(result, 0); // Replace 0 with expected result
    }

    #[test]
    fn test_part2() {
        let result = solve_part2(TEST_INPUT);
        assert_eq!(result, 0); // Replace 0 with expected result
    }

    #[test]
    fn test_specific_case() {
        // You can add more specific test cases here
        let specific_input = "test case";
        let result = solve_part1(specific_input);
        assert_eq!(result, 0);
    }
}
