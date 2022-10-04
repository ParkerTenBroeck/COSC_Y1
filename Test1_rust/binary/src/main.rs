#![no_std]
#![no_main]
#![feature(const_for)]
#![feature(strict_provenance)]
#![feature(default_alloc_error_handler)]

extern crate libm;

#[no_mangle]
/// # Safety
///
pub unsafe extern "C" fn fmod(f1: f64, f2: f64) -> f64 {
    libm::fmod(f1, f2)
}
#[no_mangle]
/// # Safety
///
pub unsafe extern "C" fn fmodf(f1: f32, f2: f32) -> f32 {
    libm::fmodf(f1, f2)
}

pub mod alloc;
pub mod asteroids;
pub mod panic_handler;
pub mod util;
pub mod asteroids2;

use asteroids::Game;
pub use interface::print;
pub use interface::println;

use num_bigint::BigUint;
use num_bigint::ToBigUint;

#[no_mangle]
pub fn main() {
    //see we dont need this silly OO talk around these parts ;)

    let mut game = Game::new();

    loop {
        game.run_frame();
    }

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

pub fn big_factorial(num: u128) -> BigUint {
    let mut big = num.to_biguint().unwrap();
    for i in 2..num {
        big *= i;
    }
    big
}
