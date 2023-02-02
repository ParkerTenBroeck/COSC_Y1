use super::movement::{AngularPlacementTrait, FirstDegreeAngularMovement, Placement};

pub struct Asteroid{
    position: Placement,
    movement: FirstDegreeAngularMovement,
}