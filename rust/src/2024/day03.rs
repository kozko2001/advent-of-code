use regex::Regex;
use std::{fs, isize, path::Path};

fn main() {
    let input = read_file_to_string("src/2024/input/day03.txt").unwrap();
    let r = solve_part1(&input);
    println!("result part 1: {}", r);

    let r = solve_part2(&input);
    println!("result part 2: {}", r);
}

fn read_file_to_string(file_path: &str) -> Result<String, std::io::Error> {
    let path = Path::new(file_path);
    fs::read_to_string(path)
}

fn solve_part1(input: &str) -> isize {
    let re = Regex::new(r"mul\((\d{1,3}),(\d{1,3})\)").unwrap();

    // Find all matches
    let sum = re
        .captures_iter(input)
        .map(|cap| {
            let num1 = &cap[1].parse::<isize>().unwrap(); // First number
            let num2 = &cap[2].parse::<isize>().unwrap(); // Second number

            num1 * num2
        })
        .sum();
    sum
}

fn solve_part2(input: &str) -> isize {
    let re = Regex::new(r"(mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\))").unwrap();

    let mut x = true;
    // Find all matches
    let sum = re
        .captures_iter(input)
        .map(|cap| {
            if cap[0].starts_with("mul") && x {
                let num1 = &cap[2].parse::<isize>().unwrap(); // First number
                let num2 = &cap[3].parse::<isize>().unwrap(); // Second number

                num1 * num2
            } else {
                x = &cap[0] == "do()";
                0
            }
        })
        .sum();
    sum
}

#[cfg(test)]
mod tests {
    use super::*;

    const TEST_INPUT: &str =
        "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
    const TEST_INPUT2: &str =
        "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";

    #[test]
    fn test_part1() {
        let result = solve_part1(TEST_INPUT);
        assert_eq!(result, 161);
    }

    #[test]
    fn test_part2() {
        let result = solve_part2(TEST_INPUT2);
        assert_eq!(result, 48);
    }
    //
    // #[test]
    // fn test_parsing_input() {
    //     let expected = vec![
    //         vec![7, 6, 4, 2, 1],
    //         vec![1, 2, 7, 8, 9],
    //         vec![9, 7, 6, 2, 1],
    //         vec![1, 3, 2, 4, 5],
    //         vec![8, 6, 4, 4, 1],
    //         vec![1, 3, 6, 7, 9],
    //     ];
    //     assert_eq!(parse_input(TEST_INPUT), expected);
    // }
}
