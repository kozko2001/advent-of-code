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

    // let r = solve_part2(&input);
    // println!("result part 2: {}", r);
}

fn read_file_to_string(file_path: &str) -> Result<String, std::io::Error> {
    let path = Path::new(file_path);
    fs::read_to_string(path)
}

fn solve_part1(input: &str) -> i32 {
    let puzzle = parse_input(input);

    let p = puzzle.iter()
        .map(|x| x)
        .into();
    println!("{}", p);

    // let (mut l1, mut l2) = parse_input(input);
    // l1.sort();
    // l2.sort();
    // zip(l1, l2).map(|(a, b)| b - a).map(|x| x.abs()).sum()
    0
}

fn solve_part2(input: &str) -> i32 {
    // let (l1, l2) = parse_input(input);
    // let count = l2.into_iter().fold(HashMap::new(), |mut map, item| {
    //     *map.entry(item).or_insert(0) += 1;
    //     map
    // });
    //
    // l1.into_iter()
    //     .fold(0, |acc, i| acc + i * count.get(&i).unwrap_or(&0))
    0
}



fn number(input: &str) -> IResult<&str, u32> {
    return map_res(digit1, str::parse)(input)
}

fn line(input: &str) -> IResult<&str, Vec<u32>> {
    return separated_list1(space1, number)(input)
}

fn matrix(input: &str) -> IResult<&str, Vec<Vec<u32>>> {
    return separated_list1(newline, line)(input)
}

fn parse_input(input: &str) -> Vec<Vec<u32>> {
    let p: IResult<&str, Vec<Vec<u32>>> = matrix(input);
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
        assert_eq!(result, 11);
    }

    #[test]
    fn test_part2() {
        let result = solve_part2(TEST_INPUT);
        assert_eq!(result, 31);
    }

    #[test]
    fn test_parsing_input() {
        let expected = vec![
            vec![7, 6, 4, 2, 1],
            vec![1, 2, 7, 8, 9],
            vec![9, 7, 6, 2, 1],
            vec![1, 3, 2, 4, 5],
            vec![8, 6, 4, 4, 1],
            vec![1, 3, 6, 7, 9],
        ];
        assert_eq!(parse_input(TEST_INPUT), expected);
    }
}
