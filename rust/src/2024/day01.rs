use std::{collections::HashMap, fs, iter::zip, path::Path};

use nom::{
    character::complete::{digit1, multispace0, multispace1, space1},
    combinator::map_res,
    multi::separated_list1,
    sequence::{preceded, separated_pair},
    IResult,
};

fn main() {
    let input = read_file_to_string("src/2024/input/day01.txt").unwrap();
    // let r = solve_part1(&input);
    // println!("result part 1: {}", r);

    let r = solve_part2(&input);
    println!("result part 2: {}", r);
}

fn read_file_to_string(file_path: &str) -> Result<String, std::io::Error> {
    let path = Path::new(file_path);
    fs::read_to_string(path)
}

fn solve_part1(input: &str) -> i32 {
    let (mut l1, mut l2) = parse_input(input);
    l1.sort();
    l2.sort();
    zip(l1, l2).map(|(a, b)| b - a).map(|x| x.abs()).sum()
}

fn solve_part2(input: &str) -> i32 {
    let (l1, l2) = parse_input(input);
    let count = l2.into_iter().fold(HashMap::new(), |mut map, item| {
        *map.entry(item).or_insert(0) += 1;
        map
    });

    l1.into_iter()
        .fold(0, |acc, i| acc + i * count.get(&i).unwrap_or(&0))
}

fn parse_input(input: &str) -> (Vec<i32>, Vec<i32>) {
    let (_, pairs) =
        preceded(multispace0, separated_list1(multispace1, parse_line))(input).unwrap();
    let (l1, l2) = pairs.into_iter().unzip();
    (l1, l2)
}

fn parse_number(input: &str) -> IResult<&str, i32> {
    map_res(digit1, str::parse)(input)
}

fn parse_line(input: &str) -> IResult<&str, (i32, i32)> {
    separated_pair(parse_number, space1, parse_number)(input)
}

#[cfg(test)]
mod tests {
    use super::*;

    const TEST_INPUT: &str = "3   4
4   3
2   5
1   3
3   9
3   3";

    #[test]
    fn test_part1() {
        let result = solve_part1(TEST_INPUT);
        assert_eq!(result, 11);
    }

    #[test]
    fn test_part2() {
        let result = solve_part2(TEST_INPUT);
        assert_eq!(result, 31);
    }

    #[test]
    fn test_parsing_input() {
        let (l1, l2) = parse_input(TEST_INPUT);
        assert_eq!(l1, vec![3, 4, 2, 1, 3, 3]);
        assert_eq!(l2, vec![4, 3, 5, 3, 9, 3])
    }
}
