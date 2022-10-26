use core::{num::NonZeroU32, fmt::Display, marker::PhantomData};

use crate::sys::*;

use super::{Class, JString};

pub struct Object(pub(super) NonZeroU32);

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