use crate::nji::object::ObjectRef;
use crate::sys::*;

pub struct Turtle;
pub type TurtleRef = ObjectRef<Turtle>;

impl TurtleRef {
    pub fn new() -> Self {
        unsafe { Self::from_id_bits(syscall_0_1::<CREATE_TURTLE>()) }
    }

    pub fn set_speed(&mut self, speed: i32) {
        unsafe { syscall_2_0::<SET_TURTLE_SPEED>(self.id_bits(), speed as u32) }
    }

    pub fn pen_down(&mut self) {
        unsafe { syscall_1_0::<TURTLE_PEN_DOWN>(self.id_bits()) }
    }

    pub fn pen_up(&mut self) {
        unsafe { syscall_1_0::<TURTLE_PEN_UP>(self.id_bits()) }
    }

    pub fn forward(&mut self, pixels: f64) {
        unsafe { syscall_3_0_1s::<TURTLE_FORWARD>(self.id_bits(), pixels.to_bits()) }
    }

    pub fn left(&mut self, pixels: f64) {
        unsafe { syscall_3_0_1s::<TURTLE_LEFT>(self.id_bits(), pixels.to_bits()) }
    }

    pub fn right(&mut self, pixels: f64) {
        unsafe { syscall_3_0_1s::<TURTLE_RIGHT>(self.id_bits(), pixels.to_bits()) }
    }
}

impl Default for TurtleRef {
    fn default() -> Self {
        Self::new()
    }
}

pub struct TurtleDisplayer;
pub type TurtleDisplayerRef = ObjectRef<TurtleDisplayer>;

impl TurtleDisplayerRef {
    #[inline(always)]
    pub fn new_with_turtle(turtle: &mut TurtleRef) -> Self {
        unsafe {
            Self::from_id_bits(syscall_1_1::<CREATE_TURTLE_DISPLAY_WITH_TURTLE>(
                turtle.id_bits(),
            ))
        }
    }

    pub fn close(self) {
        unsafe { syscall_1_0::<CLOSE_TURTLE_DISPLAYER>(self.id_bits()) }
    }
}
