#![no_std]
#![no_main]
#![feature(const_for)]
#![feature(strict_provenance)]
#![feature(default_alloc_error_handler)]

pub mod panic_handler;
pub mod alloc;
use interface::nji::{class::ClassRef, primitives::{JIntegerRef, JBooleanRef, JCharRef, JDoubleRef}};
pub use interface::*;

#[no_mangle]
pub fn main() {
    let int = JIntegerRef::new(54);
    println!("{}", int);
    let int = JCharRef::new('b');
    println!("{}", int);
    let int = JBooleanRef::new(false);
    println!("{}", int);
    let int = JDoubleRef::new(-54.2478378);
    println!("{}", int);
    let int = JIntegerRef::new(-54);
    println!("{}", int);
    let int = int.val();
    println!("{}", int);

    let class = ClassRef::for_name("java.lang.Integer").unwrap(); //turtle.get_class();
    println!("{:#?}", class.get_fields());
    println!("{:#?}", class.get_methods());
    println!("{:#?}", class.get_constructor());
}
