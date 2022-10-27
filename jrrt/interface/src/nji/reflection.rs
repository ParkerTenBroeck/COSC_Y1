use super::object::ObjectRef;


pub struct Method;
pub type MethodRef = ObjectRef<Method>;


pub struct Field;
pub type FieldRef = ObjectRef<Field>;


pub struct Constructor;
pub type ConstructorRef = ObjectRef<Constructor>;

impl ConstructorRef{
    
}