extern crate alloc;

use alloc::vec::Vec;
use rand_pcg::Pcg32;

use super::{player::Player, asteroid::Asteroid, particle::Particle, bullet::Bullet};

#[derive(Clone, Copy)]
pub struct Stats{
    bullets_fired: u32,
    asteroids_hit: u32,
    score: u32,
}

pub struct Level{
    number: u8,
    stats: Stats,
    rand: Pcg32,
    player: Option<Player>,
    asteroids: Vec<Asteroid>,
    bullets: Vec<Bullet>,
    particles: Vec<Particle>
}