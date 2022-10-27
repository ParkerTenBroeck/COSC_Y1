extern crate alloc;
use alloc::{string::String, vec::Vec};

use crate::sys::*;

use super::{
    primitives::JStringRef,
    object::{ObjectRef, ObjectArrayRef},
    reflection::{MethodRef, ConstructorRef, FieldRef},
};

pub struct Class;
pub type ClassRef = ObjectRef<Class>;


impl ClassRef {

    pub fn for_name(name: &str) -> Option<Self>{
        unsafe{
            Self::from_id_bits(
                syscall_2_1::<CLASS_FOR_NAME>(name.as_ptr() as u32, name.len() as u32)
            )
        }
    }

    pub fn get_name(&self) -> String {
        unsafe {
            let (str_id, len) = syscall_1_2::<GET_FULL_CLASS_NAME>(self.id_bits());
            let obj = JStringRef::from_id_bits(str_id).unwrap();
            obj.into_naitive_string_with_capacity(len as usize)
        }
    }

    pub fn get_methods(&self) -> Vec<MethodRef> {
        unsafe {
            let (id, len) = syscall_1_2::<GET_DECLAIRED_METHODS_OF_JVM_CLASS>(self.id_bits());
            let arr = ObjectArrayRef::from_id_bits(id).unwrap();
            let arr = arr.to_native_vec_with_capacity(len as usize);
            arr
        }
    }

    pub fn get_constructor(&self) -> Vec<ConstructorRef> {
        unsafe {
            let (id, len) = syscall_1_2::<GET_DECLAIRED_CONSTRUCTORS_OF_JVM_CLASS>(self.id_bits());
            let arr = ObjectArrayRef::from_id_bits(id).unwrap();
            let arr = arr.to_native_vec_with_capacity(len as usize);
            arr
        }
    }

    pub fn get_fields(&self) -> Vec<FieldRef> {
        unsafe {
            let (id, len) = syscall_1_2::<GET_DECLAIRED_FIELDS_OF_JVM_CLASS>(self.id_bits());
            let arr = ObjectArrayRef::from_id_bits(id).unwrap();
            let arr = arr.to_native_vec_with_capacity(len as usize);
            arr
        }
    }
}
