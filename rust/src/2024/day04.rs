use std::{char, collections::HashSet, fs, isize, path::Path, usize};

fn main() {
    let input = read_file_to_string("src/2024/input/day04.txt").unwrap();
    let r = solve_part1(&input);
    println!("result part 1: {}", r);

    let r = solve_part2(&input);
    println!("result part 2: {}", r);
}

fn read_file_to_string(file_path: &str) -> Result<String, std::io::Error> {
    let path = Path::new(file_path);
    fs::read_to_string(path)
}

struct WordSearch<'a> {
    board: Vec<String>,
    word: &'a str,
}

struct WordSearch2<'a> {
    board: Vec<String>,
    word: &'a str,
}

impl<'a> WordSearch<'a> {
    fn new(input: &str) -> Self {
        WordSearch {
            board: input.split('\n').map(String::from).collect(),
            word: "XMAS",
        }
    }

    fn positions() -> Vec<Vec<(isize, isize)>> {
        vec![
            vec![(0, 0), (1, 0), (2, 0), (3, 0)],
            vec![(0, 0), (0, 1), (0, 2), (0, 3)],
            vec![(0, 0), (1, 1), (2, 2), (3, 3)],
            vec![(0, 0), (1, -1), (2, -2), (3, -3)],
        ]
    }

    fn get_char(&self, x: usize, y: usize) -> Option<char> {
        self.board.get(y).and_then(|line| line.chars().nth(x))
    }

    fn all_points_in_board(&self) -> Box<dyn Iterator<Item = (usize, usize)>> {
        let h = self.board.len();
        let w = self.board.get(0).unwrap().len();

        Box::new((0..h).flat_map(move |row| (0..w).map(move |col| (col, row))))
    }
    fn possible_words_from<'b>(&'b self, x: usize, y: usize) -> impl Iterator<Item = String> + 'b {
        Self::positions().into_iter().filter_map(move |positions| {
            let p: Vec<(isize, isize)> = positions
                .iter()
                .map(move |(d_x, d_y)| ((*d_x + x as isize), (*d_y + y as isize)))
                .collect();
            let all_positive = p.iter().all(|(x, y)| *x >= 0 && *y >= 0);
            if !all_positive {
                None
            } else {
                let op: Option<String> = p
                    .iter()
                    .map(|(x, y)| (*x as usize, *y as usize))
                    .map(|(x, y)| self.get_char(x, y))
                    .collect();
                op
            }
        })
    }

    fn find_word(&self) -> usize {
        let p = self
            .all_points_in_board()
            .flat_map(|(x, y)| self.possible_words_from(x, y));
        p.filter(|str| str == "XMAS" || str == "SAMX").count()
    }
}

impl<'a> WordSearch2<'a> {
    fn new(input: &str) -> Self {
        WordSearch2 {
            board: input.split('\n').map(String::from).collect(),
            word: "MASMAS",
        }
    }

    fn positions() -> Vec<Vec<(isize, isize)>> {
        vec![vec![(0, 0), (1, 1), (2, 2), (0, 2), (1, 1), (2, 0)]]
    }

    fn get_char(&self, x: usize, y: usize) -> Option<char> {
        self.board.get(y).and_then(|line| line.chars().nth(x))
    }

    fn all_points_in_board(&self) -> Box<dyn Iterator<Item = (usize, usize)>> {
        let h = self.board.len();
        let w = self.board.get(0).unwrap().len();

        Box::new((0..h).flat_map(move |row| (0..w).map(move |col| (col, row))))
    }
    fn possible_words_from<'b>(&'b self, x: usize, y: usize) -> impl Iterator<Item = String> + 'b {
        Self::positions().into_iter().filter_map(move |positions| {
            let p: Vec<(isize, isize)> = positions
                .iter()
                .map(move |(d_x, d_y)| ((*d_x + x as isize), (*d_y + y as isize)))
                .collect();
            let all_positive = p.iter().all(|(x, y)| *x >= 0 && *y >= 0);
            if !all_positive {
                None
            } else {
                let op: Option<String> = p
                    .iter()
                    .map(|(x, y)| (*x as usize, *y as usize))
                    .map(|(x, y)| self.get_char(x, y))
                    .collect();
                op
            }
        })
    }

    fn find_word(&self) -> usize {
        let p = self
            .all_points_in_board()
            .flat_map(|(x, y)| self.possible_words_from(x, y));
        p.filter(|str| str == "MASMAS" || str == "SAMSAM" || str == "MASSAM" || str == "SAMMAS")
            .count()
    }
}

fn solve_part1(input: &str) -> usize {
    let board = WordSearch::new(input);

    board.find_word()
}

fn solve_part2(input: &str) -> usize {
    let board = WordSearch2::new(input);
    board.find_word()
}

#[cfg(test)]
mod tests {
    use super::*;

    const TEST_INPUT: &str = "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX";

    #[test]
    fn test_part1() {
        let result = solve_part1(TEST_INPUT);
        assert_eq!(result, 18);
    }

    #[test]
    fn test_part2() {
        let result = solve_part2(TEST_INPUT);
        assert_eq!(result, 9);
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
