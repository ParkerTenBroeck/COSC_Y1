
use num_traits::float::FloatCore;


pub type Radian = f32;

#[derive(Debug, Clone)]
pub struct Point{
    pub x: f32,
    pub y: f32,
}

impl Point{
    pub fn new(x: f32, y: f32) -> Self{
        Self { x, y }
    }

    pub fn origin() -> Self{
        Self::zero()
    }

    pub fn zero() -> Self{
        Self { x: 0.0, y: 0.0 }
    }

    pub fn from_polar(radius: f32, angle: Radian) -> Self{
        Point { 
            x: radius * libm::cosf(angle), 
            y: radius * libm::sinf(angle) 
        }
    }

    pub fn length(&self) -> f32{
        libm::sqrtf(self.x.powi(2) + self.y.powi(2))
    }

    pub fn angle(&self) -> Radian{
        libm::sqrtf(libm::atan2f(self.y, self.x))
    }

    pub fn normalize(&self) -> Self {
        let length = self.length();
        if length == 0.0{
            Self::zero()
        }else{
            Self {
                x: self.x / length,
                y: self.y / length,
            }
        }
    }

    pub fn scale(&self, factor: f32) -> Self{
        Self { 
            x: self.x * factor, 
            y: self.y * factor 
        }
    }

    pub fn add(&self, other: &Self) -> Self {
        Self { 
            x: self.x + other.x, 
            y: self.x + other.x 
        }
    }

    pub fn sub(&self, other: &Self) -> Self {
        Self { 
            x: self.x - other.x, 
            y: self.x - other.x 
        }
    }

    pub fn translate(&self, distance: f32, angle: Radian) -> Self {
        Self {
            x: self.x + distance * libm::cosf(angle),
            y: self.y + distance * libm::sinf(angle),
        }
    }

    pub fn midpoint(&self, other: &Point) -> Self {
        Self {
            x: (self.x + other.x) * 0.5,
            y: (self.y + other.y) * 0.5,
        }
    }

    pub fn distance_squared(&self, other: &Point) -> f32 {
        (other.x - self.x).powi(2) + (other.y - self.y).powi(2)
    }

    pub fn distance(&self, other: &Point) -> f32 {
        libm::sqrtf(self.distance_squared(other))
    }

    pub fn dot(&self, other: &Point) -> f32 {
        self.x * other.x + self.y * other.y
    }

    pub fn cross(&self, other: &Point) -> f32 {
        self.x * other.y - self.y * other.x
    }

    pub fn interpolate(&self, other: &Point, t: f32) -> Self {
        self.scale(1.0 - t).add(&other.scale(t))
    }

    pub fn distance_to_line(&self, a: &Point, b: &Point) -> f32 {
        let ab = b.sub(a);
        let ap = self.sub(a);
        ap.cross(&ab).abs() / ab.length()
    }

    pub fn closest_point_on_line(&self, a: &Point, b: &Point) -> Self {
        let n = b.sub(a).normalize();
        let ac = n.scale(self.sub(a).dot(&n));
        a.add(&ac)
    }

    pub fn direction_to(&self, other: &Point) -> Self {
        other.sub(self).normalize()
    }

    pub fn angle_to(&self, other: &Vector) -> f32 {
        let a = self.normalize();
        let b = other.normalize();
        libm::atan2f(a.cross(&b), a.dot(&b))
    }

    pub fn angle_between(&self, other: &Vector) -> f32 {
        libm::acosf(self.normalize().dot(&other.normalize()))
    }

    pub fn reflect(&self, normal: &Vector) -> Self {
        self.sub(&normal.scale(2.0 * self.dot(normal)))
    }

}

pub type Vector = Point;







pub struct Timer{
    expier: f32,
    elapsed: f32,
}

impl Timer{
    pub fn tick(&mut self, dt: f32){
        self.elapsed += dt;
    }
    
    pub fn has_expiered(&self) -> bool{
        self.elapsed >= self.expier
    }
}


