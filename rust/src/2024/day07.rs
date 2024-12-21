use nom::{
    character::complete::{char, digit1, space1},
    combinator::{map, map_res},
    multi::separated_list1,
    sequence::tuple,
    IResult,
};
use std::{fs, path::Path};

#[derive(Debug, Clone)]
pub struct Expr {
    total: u64,
    parts: Vec<u64>,
}

fn main() {
    let input = read_file_to_string("src/2024/input/day07.txt").unwrap();
    let r = solve_part1(&input);
    println!("result part 1: {}", r);

    let r = solve_part2(&input);
    println!("result part 2: {}", r);
}

fn read_file_to_string(file_path: &str) -> Result<String, std::io::Error> {
    let path = Path::new(file_path);
    fs::read_to_string(path)
}

fn is_possible(total: u64, acc: u64, not_used_yet: &[u64]) -> bool {
    if acc > total {
        false
    } else if not_used_yet.is_empty() {
        total == acc
    } else {
        let (first, rest) = not_used_yet.split_first().unwrap();

        let sum = acc + first;
        let mul = acc * first;

        is_possible(total, sum, rest) || is_possible(total, mul, rest)
    }
}

fn concat(a: u64, b: u64) -> u64 {
    let b_digits = (b as f64).log10() as u32 + 1;
    a * 10u64.pow(b_digits) + b
}

fn is_possible_part2(total: u64, acc: u64, not_used_yet: &[u64]) -> bool {
    if acc > total {
        false
    } else if not_used_yet.is_empty() {
        total == acc
    } else {
        let (first, rest) = not_used_yet.split_first().unwrap();

        let sum = acc + first;
        let mul = acc * first;
        let concat = concat(acc, *first);

        is_possible_part2(total, sum, rest)
            || is_possible_part2(total, mul, rest)
            || is_possible_part2(total, concat, rest)
    }
}
fn solve_part1(input: &str) -> u64 {
    let (_, puzzle) = parse(input).unwrap();

    let right = puzzle
        .iter()
        .filter(|&expr| is_possible(expr.total, 0, expr.parts.as_slice()));

    right.fold(0, |acc, x| acc + x.total)
}

fn solve_part2(input: &str) -> u64 {
    let (_, puzzle) = parse(input).unwrap();

    let right = puzzle
        .iter()
        .filter(|&expr| is_possible_part2(expr.total, 0, expr.parts.as_slice()));

    right.fold(0, |acc, x| acc + x.total)
}

fn number(input: &str) -> IResult<&str, u64> {
    map_res(digit1, str::parse)(input)
}

fn line(input: &str) -> IResult<&str, Expr> {
    map(
        tuple((
            number,                          // total
            char(':'),                       // separator
            space1,                          // whitespace
            separated_list1(space1, number), // parts
        )),
        |(total, _, _, parts)| Expr { total, parts },
    )(input)
}

fn parse(input: &str) -> IResult<&str, Vec<Expr>> {
    separated_list1(char('\n'), line)(input)
}

#[cfg(test)]
mod tests {
    use super::*;

    const TEST_INPUT: &str = "190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20";

    #[test]
    fn test_part1() {
        let result = solve_part1(TEST_INPUT);
        assert_eq!(result, 3749);
    }

    #[test]
    fn test_part2() {
        let result = solve_part2(TEST_INPUT);
        assert_eq!(result, 11387);
    }
    //
    //
    // #[test]
    // fn test_parsing_input() {
    //     let r = parse(TEST_INPUT);
    //     println!("{:?}", r);
    // }
}
