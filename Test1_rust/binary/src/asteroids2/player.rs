use super::{movement::{SecondDegreeAngularMovement, Placement, AngularMovementStep}};


pub struct Player{
    position: Placement,
    movement: SecondDegreeAngularMovement,
}

impl Player{

    pub fn step(&mut self, dt: f32){
        self.movement.step(&mut self.position, dt);
    }
}
