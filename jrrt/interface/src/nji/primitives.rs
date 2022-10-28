extern crate alloc;

use super::object::ObjectRef;
use crate::sys::*;
use alloc::string::String;

pub struct JString;
pub type JStringRef = ObjectRef<JString>;

impl JStringRef {
    pub fn into_naitive_string(self) -> String {
        unsafe {
            let len = syscall_s_s::<JVM_STRING_LENGTH>(self.id_bits());
            let mut string = String::with_capacity(len as usize);
            let ptr = string.as_mut_ptr();
            let ptr = ptr.addr();
            let len = syscall_sss_s::<COPY_FROM_JVM_STRING>(self.id_bits(), ptr as u32, len);
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
                syscall_sss_s::<COPY_FROM_JVM_STRING>(self.id_bits(), ptr as u32, capacity as u32);
            string.as_mut_vec().set_len(len as usize);
            string
        }
    }
}

// -------------------------------------------------

pub struct JBoolean;
pub type JBooleanRef = ObjectRef<JBoolean>;

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

macro_rules! auto_impls {
    ($refname:ident, $rtype:ident, $jtype:ident) => {
        auto_impls!($refname, $rtype, $jtype, res, { res as $rtype });
    };

    ($refname:ident, $rtype:ident, $jtype:ident, $into_rtype_val:ident, $into_rtype:expr) => {
        auto_impls!(
            $refname,
            $rtype,
            $jtype,
            $into_rtype_val,
            $into_rtype,
            val,
            { val as u32 }
        );
    };

    ($refname:ident, $rtype:ident, $jtype:ident, $into_rtype_val:ident, $into_rtype:expr, $val_rtype_val:ident, $val_rtype:expr) => {
        auto_impls!(
            $refname,
            $rtype,
            $jtype,
            $into_rtype_val,
            $into_rtype,
            $val_rtype_val,
            $val_rtype,
            syscall_s_s,
            syscall_s_s
        );
    };

    ($refname:ident, $rtype:ident, $jtype:ident, $into_rtype_val:ident, $into_rtype:expr, $val_rtype_val:ident, $val_rtype:expr, $syscall_create:ident, $syscall_get:ident) => {
        impl $refname {
            pub fn new($val_rtype_val: $rtype) -> Self {
                unsafe {
                    Self::from_id_bits_unchecked($syscall_create::<
                        concat_idents!(CREATE_JVM_, $jtype),
                    >($val_rtype))
                }
            }

            pub fn val(&self) -> $rtype {
                unsafe {
                    let $into_rtype_val =
                        $syscall_get::<concat_idents!(GET_JVM_, $jtype)>(self.id_bits());
                    $into_rtype
                }
            }
        }

        impl From<$rtype> for $refname {
            fn from(val: $rtype) -> Self {
                Self::new(val)
            }
        }

        impl From<$refname> for $rtype {
            fn from(val: $refname) -> Self {
                val.val()
            }
        }
    };
}

auto_impls!(JBooleanRef, bool, BOOLEAN, res, res != 0);
auto_impls!(JByteRef, i8, BYTE);
auto_impls!(JShortRef, i16, SHORT);
auto_impls!(JCharRef, char, CHAR, res, char::from_u32_unchecked(res));
auto_impls!(JIntegerRef, i32, INTEGER);
auto_impls!(
    JLongRef,
    i64,
    LONG,
    res,
    res as i64,
    val,
    val as u64,
    syscall_d_s,
    syscall_s_d
);
auto_impls!(
    JFloatRef,
    f32,
    FLOAT,
    res,
    f32::from_bits(res),
    val,
    f32::to_bits(val)
);
auto_impls!(
    JDoubleRef,
    f64,
    DOUBLE,
    res,
    f64::from_bits(res),
    val,
    f64::to_bits(val),
    syscall_d_s,
    syscall_s_d
);
