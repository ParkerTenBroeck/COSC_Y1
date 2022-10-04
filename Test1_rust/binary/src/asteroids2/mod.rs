use self::{level::Level, util::Timer};

mod asteroid;
mod bullet;
mod level;
mod particle;
mod player;
mod util;
mod movement;
mod interface;




pub struct Game{
    high_score: u32,
    state: GameState,
}

enum GameState{
    IntroScreen{
        
    },
    ActiveLevel{
        score: u32,
        level: Level,
        state: LevelState,
    },
    GameOver{
        level: Level,
    }
}

enum LevelState{
    Playing,
    Cleared{ timer: Timer },
    Destroyed{ timer: Timer },
}