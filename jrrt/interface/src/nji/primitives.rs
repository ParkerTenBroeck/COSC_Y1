extern crate alloc;
use alloc::string::String;
use super::object::ObjectRef;
use crate::sys::*;



pub struct JString;
pub type JStringRef = ObjectRef<JString>;

impl JStringRef {
    
    pub fn into_naitive_string(self) -> String {
        unsafe {
            let len = syscall_1_1::<JVM_STRING_LENGTH>(self.id_bits());
            let mut string = String::with_capacity(len as usize);
            let ptr = string.as_mut_ptr();
            let ptr = ptr.addr();
            let len = syscall_3_1::<COPY_FROM_JVM_STRING>(self.id_bits(), ptr as u32, len);
            string.as_mut_vec().set_len(len as usize);

            string
        }
    }

    pub fn into_naitive_string_with_capacity(self, capacity: usize) -> String {
        unsafe {
            let mut string = String::with_capacity(capacity);
            let ptr = string.as_mut_ptr();
            let ptr = ptr.addr();
            let len =
                syscall_3_1::<COPY_FROM_JVM_STRING>(self.id_bits(), ptr as u32, capacity as u32);
            string.as_mut_vec().set_len(len as usize);
            string
        }
    }
}

// -------------------------------------------------



pub struct JByte;
pub type JByteRef = ObjectRef<JByte>;

pub struct JShort;
pub type JShortRef = ObjectRef<JShort>;


pub struct JChar;
pub type JCharRef = ObjectRef<JChar>;

pub struct JInteger;
pub type JIntegerRef = ObjectRef<JInteger>;


pub struct JFloat;
pub type JFloatRef = ObjectRef<JFloat>;

pub struct JLong;
pub type JLongRef = ObjectRef<JLong>;

pub struct JDouble;
pub type JDoubleRef = ObjectRef<JDouble>;