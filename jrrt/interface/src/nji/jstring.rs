extern crate alloc;
use alloc::string::String;

use super::object::Object;
use crate::sys::*;

pub struct JString(Object);

impl JString {
    /// # Safety
    ///
    /// must be a valid NJI id that represents a java String
    pub unsafe fn new(id: u32) -> Self {
        Self(Object::new_p(id))
    }

    pub fn to_naitive_string(self) -> String {
        unsafe {
            let len = syscall_1_1::<JVM_STRING_LENGTH>(self.0.id_bits());
            let mut string = String::with_capacity(len as usize);
            let ptr = string.as_mut_ptr();
            let ptr = ptr.addr();
            let len = syscall_3_1::<COPY_FROM_JVM_STRING>(self.0.id_bits(), ptr as u32, len);
            string.as_mut_vec().set_len(len as usize);

            string
        }
    }

    pub fn to_naitive_string_with_capacity(self, capacity: usize) -> String {
        unsafe {
            let mut string = String::with_capacity(capacity);
            let ptr = string.as_mut_ptr();
            let ptr = ptr.addr();
            let len =
                syscall_3_1::<COPY_FROM_JVM_STRING>(self.0.id_bits(), ptr as u32, capacity as u32);
            string.as_mut_vec().set_len(len as usize);
            string
        }
    }
}
