use std::{fs, path::Path};

use nom::{
    character::complete::{digit1, newline, space1},
    combinator::map_res,
    multi::separated_list1,
    IResult,
};

fn main() {
    let input = read_file_to_string("src/2024/input/day02.txt").unwrap();
    let r = solve_part1(&input);
    println!("result part 1: {}", r);

    let r = solve_part2(&input);
    println!("result part 2: {}", r);
}

fn read_file_to_string(file_path: &str) -> Result<String, std::io::Error> {
    let path = Path::new(file_path);
    fs::read_to_string(path)
}

fn solve_part1(input: &str) -> usize {
    let puzzle = parse_input(input);
    let p = puzzle
        .iter()
        .map(|line| {
            let diffs: Vec<i32> = line
                .windows(2)
                .map(|x| (x[1] as i32) - (x[0] as i32))
                .collect();
            let is_monotonic = diffs.iter().all(|&x| x >= 0) || diffs.iter().all(|&x| x <= 0);
            let has_valid_jumps = diffs.iter().all(|x| x.abs() <= 3 && x.abs() >= 1);

            is_monotonic && has_valid_jumps
        })
        .filter(|&x| x)
        .count();
    p
}

fn remove_one_combinations<T: Clone>(vec: &[T]) -> Vec<Vec<T>> {
    (0..vec.len())
        .map(|i| {
            vec.iter()
                .enumerate()
                .filter(|(j, _)| *j != i)
                .map(|(_, x)| x.clone())
                .collect()
        })
        .collect()
}
fn solve_part2(input: &str) -> usize {
    let puzzle = parse_input(input);
    let p = puzzle
        .iter()
        .map(|line| {
            let all_perms = remove_one_combinations(line);

            let x = all_perms.into_iter().any(|line| {
                let diffs: Vec<i32> = line
                    .windows(2)
                    .map(|x| (x[1] as i32) - (x[0] as i32))
                    .collect();
                let is_monotonic = diffs.iter().all(|&x| x >= 0) || diffs.iter().all(|&x| x <= 0);
                let has_valid_jumps = diffs.iter().all(|x| x.abs() <= 3 && x.abs() >= 1);

                is_monotonic && has_valid_jumps
            });
            x
        })
        .filter(|&x| x)
        .count();
    p
}

fn number(input: &str) -> IResult<&str, u32> {
    map_res(digit1, str::parse)(input)
}

fn line(input: &str) -> IResult<&str, Vec<u32>> {
    separated_list1(space1, number)(input)
}

fn matrix(input: &str) -> IResult<&str, Vec<Vec<u32>>> {
    separated_list1(newline, line)(input)
}

fn parse_input(input: &str) -> Vec<Vec<u32>> {
    let p = matrix(input);
    let (_, a) = p.unwrap();
    a
}

#[cfg(test)]
mod tests {
    use super::*;

    const TEST_INPUT: &str = "7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9";

    #[test]
    fn test_part1() {
        let result = solve_part1(TEST_INPUT);
        assert_eq!(result, 2);
    }

    #[test]
    fn test_part2() {
        let result = solve_part2(TEST_INPUT);
        assert_eq!(result, 4);
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
