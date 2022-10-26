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