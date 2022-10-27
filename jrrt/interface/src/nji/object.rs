use core::{
    any::type_name,
    fmt::{Debug, Display},
    marker::PhantomData,
    num::NonZeroU32,
};

extern crate alloc;
use alloc::string::String;

use crate::sys::*;

use super::{class::ClassRef, jstring::JStringRef};

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
}

pub struct ObjectRef<T>(Object, PhantomData<T>);

impl<T> ObjectRef<T> {
    /// # Safety
    ///
    /// Must be a valid obj_id
    pub unsafe fn from_obj(obj_id: Object) -> Self {
        Self(obj_id, PhantomData)
    }

    /// # Safety
    ///
    /// Must be a valid obj_id
    pub unsafe fn from_id_bits(obj_id: u32) -> Self {
        Self(Object::new_p(obj_id), PhantomData)
    }

    pub fn id_bits(&self) -> u32 {
        self.0 .0.into()
    }

    pub fn get_class(&self) -> ClassRef {
        unsafe {
            let obj = Object::new_p(syscall_1_1::<GET_OBJECT_CLASS>(self.id_bits()));
            ClassRef::from_obj(obj)
        }
    }

    pub fn to_naitive_string(&self) -> String {
        unsafe {
            let (str_id, len) = syscall_1_2::<JVM_OBJECT_TO_STRING>(self.id_bits());
            let str = JStringRef::from_id_bits(str_id);
            str.into_naitive_string_with_capacity(len as usize)
        }
    }
}

impl<T> Display for ObjectRef<T> {
    fn fmt(&self, f: &mut core::fmt::Formatter<'_>) -> core::fmt::Result {
        write!(f, "{}", self.to_naitive_string())
    }
}

impl<T> Debug for ObjectRef<T> {
    fn fmt(&self, f: &mut core::fmt::Formatter<'_>) -> core::fmt::Result {
        f.debug_struct(type_name::<T>())
            .field("ref_id", &self.0.id_bits())
            .field("native_repr", &self.to_naitive_string())
            .finish()
    }
}

impl Drop for Object {
    fn drop(&mut self) {
        unsafe {
            syscall_1_0::<FREE_JVM_OBJECT>(self.0.try_into().unwrap());
        }
    }
}


pub struct ObjectArray;
pub type ObjectArrayRef = ObjectRef<ObjectArray>;

impl ObjectArrayRef{
    
}
