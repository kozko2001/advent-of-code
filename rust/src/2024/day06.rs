use nom::{
    branch::alt,
    character::complete::{char, newline},
    combinator::map,
    multi::{many1, separated_list1},
    IResult,
};
use rayon::iter::{IntoParallelRefIterator as _, ParallelIterator};
use std::{
    collections::{HashMap, HashSet},
    fs, isize,
    path::Path,
};

#[derive(Debug, Clone, PartialEq)]
pub enum Tile {
    Empty,
    Wall,
    Person,
}

#[derive(Debug)]
pub struct Maze {
    grid: HashMap<(i32, i32), Tile>,
    person_pos: (i32, i32),
    width: i32,
    height: i32,
}

#[derive(Debug, Clone, Copy, PartialEq, Hash, Eq)]
enum Direction {
    North,
    East,
    South,
    West,
}

#[derive(Debug, Hash, Eq, PartialEq, Clone, Copy)]
struct VisitState {
    position: (i32, i32),
    direction: Direction,
}

impl Direction {
    fn rotate_clockwise(&self) -> Self {
        match self {
            Direction::North => Direction::East,
            Direction::East => Direction::South,
            Direction::South => Direction::West,
            Direction::West => Direction::North,
        }
    }

    fn next_position(&self, current: (i32, i32)) -> (i32, i32) {
        match self {
            Direction::North => (current.0, current.1 - 1),
            Direction::East => (current.0 + 1, current.1),
            Direction::South => (current.0, current.1 + 1),
            Direction::West => (current.0 - 1, current.1),
        }
    }
}

impl Maze {
    pub fn new(
        grid: HashMap<(i32, i32), Tile>,
        person_pos: (i32, i32),
        width: i32,
        height: i32,
    ) -> Self {
        Maze {
            grid,
            person_pos,
            width,
            height,
        }
    }

    // Optional: method to get tile at a specific position
    pub fn get_tile(&self, x: i32, y: i32) -> Option<&Tile> {
        self.grid.get(&(x, y))
    }

    fn is_valid_position(&self, pos: (i32, i32)) -> bool {
        pos.0 >= 0 && pos.0 < self.width && pos.1 >= 0 && pos.1 < self.height
    }

    fn traverse(&self) -> Result<Vec<(i32, i32)>, &'static str> {
        let mut current_pos = self.person_pos;
        let mut current_direction = Direction::North;
        let mut visited_positions = vec![current_pos];

        let mut visited_states = HashSet::new();

        visited_states.insert(VisitState {
            position: current_pos,
            direction: current_direction,
        });
        loop {
            let next_pos = current_direction.next_position(current_pos);

            // Check if next position is out of bounds
            if !self.is_valid_position(next_pos) {
                break;
            }

            // Check if next position is a wall
            match self.get_tile(next_pos.0, next_pos.1) {
                Some(Tile::Wall) => {
                    // Rotate clockwise
                    current_direction = current_direction.rotate_clockwise();
                }
                Some(Tile::Empty) => {
                    // Move to next position
                    current_pos = next_pos;
                    visited_positions.push(current_pos);

                    // Check for loop
                    let current_state = VisitState {
                        position: current_pos,
                        direction: current_direction,
                    };

                    if !visited_states.insert(current_state) {
                        return Err("Loop detected");
                    }
                }
                Some(Tile::Person) => {
                    panic!("cannot crash into a person");
                }
                None => break, // Out of maze bounds
            }

            // Prevent infinite loop (optional, depends on problem requirements)
            if visited_positions.len() > (self.width * self.height) as usize {
                break;
            }
        }

        Ok(visited_positions)
    }
}

// Parsing functions

fn parse_tile(input: &str) -> IResult<&str, Tile> {
    alt((
        map(char('.'), |_| Tile::Empty),
        map(char('#'), |_| Tile::Wall),
        map(char('^'), |_| Tile::Person),
    ))(input)
}

fn parse_row(input: &str) -> IResult<&str, Vec<Tile>> {
    many1(parse_tile)(input)
}

fn parse_maze(input: &str) -> IResult<&str, Maze> {
    let (remaining, rows) = separated_list1(newline, parse_row)(input)?;

    // Convert 2D Vec to HashMap and find person position
    let mut grid = HashMap::new();
    let mut person_pos = None;

    for (y, row) in rows.iter().enumerate() {
        for (x, tile) in row.iter().enumerate() {
            let coords = (x as i32, y as i32);

            match tile {
                Tile::Person => {
                    person_pos = Some(coords);
                    grid.insert(coords, Tile::Empty); // Person stands on Empty
                }
                Tile::Wall | Tile::Empty => {
                    grid.insert(coords, tile.clone());
                }
            }
        }
    }

    Ok((
        remaining,
        Maze::new(
            grid,
            person_pos.expect("No person found in maze"),
            rows[0].len() as i32,
            rows.len() as i32,
        ),
    ))
}

fn main() {
    let input = read_file_to_string("src/2024/input/day06.txt").unwrap();
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
    let (_, maze) = parse_maze(input).expect("Failed to parse maze");
    let visited = maze.traverse().unwrap();

    let distinct_positions: HashSet<_> = visited.iter().cloned().collect();
    distinct_positions.len() as isize
}

fn solve_part2(input: &str) -> usize {
    let (_, maze) = parse_maze(input).expect("Failed to parse maze");
    let visited = maze.traverse().unwrap();

    let distinct_positions: Vec<_> = visited.to_vec();

    distinct_positions
        .par_iter()
        .map(|&(x, y)| {
            let mut new_grid = maze.grid.clone();
            new_grid.insert((x, y), Tile::Wall);
            Maze::new(new_grid, maze.person_pos, maze.width, maze.height)
        })
        .filter(|maze| {
            let ok = maze.traverse();
            ok.is_err()
        })
        .count()
}

#[cfg(test)]
mod tests {
    use super::*;

    const TEST_INPUT: &str = "....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...";

    #[test]
    fn test_part1() {
        let result = solve_part1(TEST_INPUT);

        assert_eq!(result, 41);
    }

    #[test]
    fn test_part2() {
        let result = solve_part2(TEST_INPUT);
        assert_eq!(result, 6);
    }

    // #[test]
    // fn test_parsing_input() {
    //     let maze = parse_maze(TEST_INPUT);
    //     println!("{:?}", maze)
    // }
}
