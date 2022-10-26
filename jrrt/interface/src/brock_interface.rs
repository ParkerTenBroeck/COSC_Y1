use crate::nji::object::Object;
use crate::sys::*;

pub struct Turtle(Object);

impl Turtle {
    pub fn new() -> Self {
        unsafe { Self(Object::new_p(syscall_0_1::<CREATE_TURTLE>())) }
    }

    pub fn set_speed(&mut self, speed: i32) {
        unsafe { syscall_2_0::<SET_TURTLE_SPEED>(self.0.id_bits(), speed as u32) }
    }

    pub fn pen_down(&mut self) {
        unsafe { syscall_1_0::<TURTLE_PEN_DOWN>(self.0.id_bits()) }
    }

    pub fn pen_up(&mut self) {
        unsafe { syscall_1_0::<TURTLE_PEN_UP>(self.0.id_bits()) }
    }

    pub fn forward(&mut self, pixels: f64) {
        unsafe { syscall_3_0_1s::<TURTLE_FORWARD>(self.0.id_bits(), pixels.to_bits()) }
    }

    pub fn left(&mut self, pixels: f64) {
        unsafe { syscall_3_0_1s::<TURTLE_LEFT>(self.0.id_bits(), pixels.to_bits()) }
    }

    pub fn right(&mut self, pixels: f64) {
        unsafe { syscall_3_0_1s::<TURTLE_RIGHT>(self.0.id_bits(), pixels.to_bits()) }
    }

    pub fn obj_mut(&mut self) -> &Object {
        &mut self.0
    }
}

impl Default for Turtle {
    fn default() -> Self {
        Self::new()
    }
}

pub struct TurtleDisplayer(Object);

impl TurtleDisplayer {
    #[inline(always)]
    pub fn new_with_turtle(turtle: &mut Turtle) -> Self {
        unsafe {
            Self(Object::new_p(syscall_1_1::<
                CREATE_TURTLE_DISPLAY_WITH_TURTLE,
            >(turtle.0.id_bits())))
        }
    }

    pub fn close(self) {
        unsafe { syscall_1_0::<CLOSE_TURTLE_DISPLAYER>(self.0.id_bits()) }
    }
}
