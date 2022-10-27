extern crate alloc;
use alloc::{string::String, vec::Vec};

use crate::sys::*;

use super::{
    jstring::JStringRef,
    object::{Object, ObjectRef},
    ObjectArray, MethodRef, ConstructorRef, FieldRef,
};

pub struct Class;
pub type ClassRef = ObjectRef<Class>;

impl ClassRef {
    pub fn get_name(&self) -> String {
        unsafe {
            let (str_id, len) = syscall_1_2::<GET_FULL_CLASS_NAME>(self.id_bits());
            let obj = JStringRef::from_id_bits(str_id);
            obj.into_naitive_string_with_capacity(len as usize)
        }
    }

    pub fn get_methods(&self) -> Vec<MethodRef> {
        unsafe {
            let (id, len) = syscall_1_2::<GET_DECLAIRED_METHODS_OF_JVM_CLASS>(self.id_bits());
            let arr = ObjectArray::new(Object::new_p(id));
            let arr = arr.to_vec_with_capacity(len as usize);
            core::mem::transmute(arr)
        }
    }

    pub fn get_constructor(&self) -> Vec<ConstructorRef> {
        unsafe {
            let (id, len) = syscall_1_2::<GET_DECLAIRED_CONSTRUCTORS_OF_JVM_CLASS>(self.id_bits());
            let arr = ObjectArray::new(Object::new_p(id));
            let arr = arr.to_vec_with_capacity(len as usize);
            core::mem::transmute(arr)
        }
    }

    pub fn get_fields(&self) -> Vec<FieldRef> {
        unsafe {
            let (id, len) = syscall_1_2::<GET_DECLAIRED_FIELDS_OF_JVM_CLASS>(self.id_bits());
            let arr = ObjectArray::new(Object::new_p(id));
            let arr = arr.to_vec_with_capacity(len as usize);
            core::mem::transmute(arr)
        }
    }
}
