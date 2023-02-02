use super::util::{Point, Vector, Radian};

pub trait MovementStep<T: PositionalPlacementTrait>{
    fn step(&mut self, placement: &mut T, dt: f32);
}

pub trait AngularMovementStep<T: PositionalPlacementTrait + AngularPlacementTrait>{
    fn step(&mut self, placement: &mut T, dt: f32);
}


pub struct FirstDegreeMovement {
    velocity: Vector,
}

impl<T: PositionalPlacementTrait> MovementStep<T> for FirstDegreeMovement{
    fn step(&mut self, placement: &mut T, dt: f32) {
        let d_p = self.velocity.scale(dt);
        let p = placement.position_mut();
        *p = p.add(&d_p);
    }
}

pub struct FirstDegreeAngularMovement {
    velocity: Vector,
    angular_velocity: f32,
}

impl<T: PositionalPlacementTrait + AngularPlacementTrait> AngularMovementStep<T> for FirstDegreeAngularMovement{
    fn step(&mut self, placement: &mut T, dt: f32) {
        let d_a = self.angular_velocity * dt;
        let a = placement.angle_mut();
        *a += d_a;

        let d_p = self.velocity.scale(dt);
        let p = placement.position_mut();
        *p = p.add(&d_p);
    }
}

pub struct SecondDegreeAngularMovement {
    velocity: Vector,
    acceleration: Vector,
    angular_velocity: f32,
}

impl<T: PositionalPlacementTrait + AngularPlacementTrait> AngularMovementStep<T> for SecondDegreeAngularMovement{
    fn step(&mut self, placement: &mut T, dt: f32) {
        let d_a = self.angular_velocity * dt;
        let a = placement.angle_mut();
        *a += d_a;

        let t_half = self.acceleration.scale(dt / 2.0);

        self.velocity = self.velocity.add(&t_half);

        let d_p = self.velocity.scale(dt);
        let p = placement.position_mut();
        *p = p.add(&d_p);

        self.velocity = self.velocity.add(&t_half);
    }
}


// -----------------------------------------------------------------------------------

pub trait PositionalPlacementTrait{
    fn position(&self) -> &Point;
    fn position_mut(&mut self) -> &mut Point;
}

pub trait AngularPlacementTrait{
    fn angle(&self) -> &f32;
    fn angle_mut(&mut self) -> &mut f32;
}

pub struct Position{
    position: Point,
}

impl PositionalPlacementTrait for Position{
    fn position(&self) -> &Point {
        &self.position
    }

    fn position_mut(&mut self) -> &mut Point {
        &mut self.position
    }
}

pub struct Placement{
    position: Point,
    angle: Radian
}

impl PositionalPlacementTrait for Placement{
    fn position(&self) -> &Point {
        &self.position
    }

    fn position_mut(&mut self) -> &mut Point {
        &mut self.position
    }
}

impl AngularPlacementTrait for Placement{
    fn angle(&self) -> &f32 {
        &self.angle
    }

    fn angle_mut(&mut self) -> &mut f32 {
        &mut self.angle
    }
}


// -----------------------------------------------------------------------------------