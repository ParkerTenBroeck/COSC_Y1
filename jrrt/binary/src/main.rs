#![no_std]
#![no_main]
#![feature(const_for)]
#![feature(strict_provenance)]
#![feature(default_alloc_error_handler)]

use alloc::boxed::Box;
use alloc::vec::Vec;
use interface::*;

pub mod alloc;
pub mod panic_handler;

extern crate libm;

enum IdkWhat<D, const N: usize = 5> {
    Local { local: [Option<D>; N] },
    Vec { non_local: Vec<D> },
}

impl<D: Copy, const N: usize> IdkWhat<D, N> {
    pub fn new() -> Self {
        Self::Local { local: [None; N] }
    }

    pub fn len(&self) -> usize {
        match self {
            IdkWhat::Local { local } => local.iter().filter(|o| o.is_some()).count(),
            IdkWhat::Vec { non_local } => non_local.len(),
        }
    }

    pub fn push(&mut self, data: D) {
        match self {
            IdkWhat::Local { local } => {
                for next in local.iter_mut() {
                    if next.is_none() {
                        *next = Some(data);
                        return;
                    }
                }
                let mut vec = Vec::new();
                for next in (*local).into_iter() {
                    if let Some(next) = next {
                        vec.push(next);
                    } else {
                        break;
                    }
                }
                *self = Self::Vec { non_local: vec };
            }
            IdkWhat::Vec { ref mut non_local } => {
                non_local.push(data);
            }
        }
    }

    pub fn new_with(data: D) -> Self {
        let mut new = Self::new();
        new.push(data);
        new
    }
}

enum Node<D> {
    Leaf(IdkWhat<D>),
    QUad {
        tl: Option<Box<Node<D>>>,
        tr: Option<Box<Node<D>>>,
        bl: Option<Box<Node<D>>>,
        br: Option<Box<Node<D>>>,
    },
}

impl<D: Copy> Node<D> {
    pub fn is_leaf(&self) -> bool {
        matches!(self, Node::Leaf(_))
    }

    pub fn new_leaf(data: D) -> Self {
        Node::Leaf(IdkWhat::new_with(data))
    }

    pub fn push_data(&mut self, data: D) {
        match self {
            Node::Leaf(sv) => {}
            Node::QUad { tl, tr, bl, br } => {}
        }
    }
}

struct BinaryTreeIsh {
    tree: Option<Node<Vector>>,
}

struct Compare2D {
    x: core::cmp::Ordering,
    y: core::cmp::Ordering,
}

impl Compare2D {}

#[derive(Clone, Copy, PartialEq, PartialOrd)]
struct Vector {
    pub x: f32,
    pub y: f32,
}

impl BinaryTreeIsh {
    pub fn new() -> Self {
        Self { tree: None }
    }

    pub fn push(&mut self, new_data: Vector) {
        if let Some(tree) = &mut self.tree {
            tree.push_data(new_data);
        } else {
            self.tree = Some(Node::new_leaf(new_data));
        }
    }
}

#[no_mangle]
pub fn main() {
    let mut screen = interface::screen::Screen::new();

    // lazy_static::lazy_static! {
    //     static ref OBJECT: interface::nji::Class = {
    //         //interface::nji::Class::new("");
    //         panic!();
    //     };
    // }
    let turtle = interface::brock_interface::TurtleRef::new();
    //let display = interface::brock_interface::TurtleDisplayerRef::new_with_turtle(&mut turtle);
    let class = turtle.get_class();
    println!("{:#?}", class.get_fields());
    println!("{:#?}", class.get_methods());
    println!("{:#?}", class.get_constructor());
    //turtle.forward(100.0);
    //display.close();

    if true {
        panic!();
    }

    loop {
        use screen::ScreenCommand::*;
        screen.push_command(SetColor([0, 0, 0, 255]));
        screen.push_command(Clear);
        screen.send_draw_call();
        interface::sys::sleep_d_ms(33);
        if interface::sys::is_key_pressed('q') {
            interface::sys::halt();
        }
    }

    macros::java! {
    ASD SD s ss
            * / .
        }

    // use alloc::rc::Rc;
    // use interface::brock_interface::{Turtle, TurtleDisplayer};
    // println!("Hello world!");
    // println!("Factorial 34 {}", factorial(34));

    // let rc = Rc::new(spin::RwLock::new(12));
    // *rc.write() += 2;

    // println!("float: {} magic", 12.3);

    // println!("\n\nThis should display in a separate window.");

    // use core::f64::consts::*;

    // let mut yertle = Turtle::new();

    // let obj = yertle.obj_mut();
    // let class = obj.get_class();
    // let fields = class.get_fields();
    // for f in fields {
    //     println!("{}", f);
    // }
    // let name = class.get_name();
    // println!("Turtle class name: {}: {}", name, obj);

    // yertle.set_speed(0);
    // let display = TurtleDisplayer::new_with_turtle(&mut yertle);
    // yertle.pen_down();
    // yertle.right(PI / 2.0);
    // yertle.forward(100.0);
    // yertle.left(PI);
    // yertle.forward(50.0);
    // yertle.right(PI / 2.0);
    // yertle.forward(50.0);
    // yertle.right(PI / 2.0);
    // yertle.forward(50.0);
    // yertle.left(PI / 2.0);
    // yertle.pen_up();
    // yertle.forward(50.0);
    // yertle.left(PI / 2.0);
    // yertle.pen_down();
    // yertle.forward(50.0);
    // yertle.pen_up();
    // yertle.forward(25.0);
    // yertle.pen_down();
    // yertle.forward(25.0);
    // display.close();
}

pub fn factorial(num: u128) -> u128 {
    let mut rolling = num;
    for i in 2..num {
        rolling *= i;
    }
    rolling
}
