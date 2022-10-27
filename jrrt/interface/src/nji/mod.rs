use core::num::NonZeroU32;

extern crate alloc;

use alloc::vec::Vec;

use crate::sys::*;

pub mod class;
pub mod jstring;
pub mod object;
use object::*;

// -----------------------------------------------------

pub struct ObjectArray(Object);

impl ObjectArray {
    /// # Safety
    ///
    /// Must be a valid obj_id AND must be an array
    pub unsafe fn new(obj: Object) -> Self {
        Self(obj)
    }

    pub fn id(&self) -> NonZeroU32 {
        self.0 .0
    }

    pub fn len(&self) -> usize {
        unsafe { syscall_1_1::<JVM_ARRAY_LENGTH>(self.0.id_bits()) as usize }
    }

    pub fn is_empty(&self) -> bool {
        self.len() == 0
    }

    pub fn to_vec(self) -> Vec<Object> {
        let len = self.len();
        let mut vec = Vec::with_capacity(len);
        let ptr: *mut Object = vec.as_mut_ptr();
        let ptr = ptr.addr();
        unsafe {
            let len =
                syscall_3_1::<MOVE_INTO_NAITIVE_ARRAY>(self.0.id_bits(), ptr as u32, len as u32);
            vec.set_len(len as usize);
            vec
        }
    }

    pub fn to_vec_with_capacity(self, capacity: usize) -> Vec<Object> {
        let mut vec = Vec::with_capacity(capacity);
        let ptr: *mut Object = vec.as_mut_ptr();
        let ptr = ptr.addr();
        unsafe {
            let len = syscall_3_1::<MOVE_INTO_NAITIVE_ARRAY>(
                self.0.id_bits(),
                ptr as u32,
                capacity as u32,
            );
            vec.set_len(len as usize);
            vec
        }
    }
}

// -----------------------------------------------------

// -----------------------------------------------------

pub struct Method;
pub type MethodRef = ObjectRef<Method>;


pub struct Field;
pub type FieldRef = ObjectRef<Field>;


pub struct Constructor;
pub type ConstructorRef = ObjectRef<Constructor>;