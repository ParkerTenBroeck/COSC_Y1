use core::{fmt::Display, num::NonZeroU32};

extern crate alloc;

use alloc::{string::String, vec::Vec};

use crate::sys::*;

pub struct Object(NonZeroU32);

impl Object {
    /// # Safety
    ///
    /// Must be a valid obj_id
    pub unsafe fn new(obj_id: NonZeroU32) -> Self {
        Self(obj_id)
    }

    /// # Safety
    ///
    /// Must be a valid obj_id
    pub unsafe fn new_p(obj_id: u32) -> Self {
        Self(obj_id.try_into().expect("Invalid NULL id given"))
    }

    pub fn id_bits(&self) -> u32 {
        self.0.into()
    }

    pub fn get_class(&self) -> Class {
        let obj = unsafe { Self::new_p(syscall_1_1::<GET_OBJECT_CLASS>(self.0.into())) };
        Class(obj)
    }
}

impl Display for Object {
    fn fmt(&self, f: &mut core::fmt::Formatter<'_>) -> core::fmt::Result {
        unsafe {
            let (str_id, len) = syscall_1_2::<JVM_OBJECT_TO_STRING>(self.id_bits());
            let str = JString::new(str_id);
            write!(f, "{}", str.to_naitive_string_with_capacity(len as usize))
        }
    }
}

impl Drop for Object {
    fn drop(&mut self) {
        unsafe {
            syscall_1_0::<FREE_JVM_OBJECT>(self.0.try_into().unwrap());
        }
    }
}

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

// -----------------------------------------------------

pub struct Class(Object);

impl Class {
    pub fn get_name(&self) -> String {
        unsafe {
            let (str_id, len) = syscall_1_2::<GET_FULL_CLASS_NAME>(self.0.id_bits());
            let obj = JString::new(str_id);
            obj.to_naitive_string_with_capacity(len as usize)
        }
    }

    pub fn get_methods(&self) -> Vec<Method> {
        panic!();
    }

    pub fn get_constructor(&self) -> Vec<Constructor> {
        panic!();
    }

    pub fn get_fields(&self) -> Vec<Field> {
        unsafe {
            let (id, len) = syscall_1_2::<GET_DECLAIRED_FIELDS_OF_JVM_CLASS>(self.0.id_bits());
            let arr = ObjectArray::new(Object::new_p(id));
            let arr = arr.to_vec_with_capacity(len as usize);
            core::mem::transmute(arr)
        }
    }
}

// -----------------------------------------------------

pub struct Method(Object);
pub struct Field(Object);

impl Field {}

impl Display for Field {
    fn fmt(&self, f: &mut core::fmt::Formatter<'_>) -> core::fmt::Result {
        self.0.fmt(f)
    }
}

pub struct Constructor(Object);
