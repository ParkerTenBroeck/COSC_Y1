use interface::screen::{Screen, ScreenCommand, ScreenVec};



pub struct Interface{
    screen: Screen,
    show_debug: bool,
}

impl Interface{


    pub fn screen_bounds(&self) -> ScreenVec{
        (self.screen.get_width(), self.screen.get_height()).into()
    }

    pub fn draw(&mut self, command: ScreenCommand){
        self.draw(command)
    }

    pub fn draw_wrapping(&mut self, command: ScreenCommand){
        self.draw_wrapping(command)
    }

    pub fn draw_debug(&mut self, command: ScreenCommand){
        if self.show_debug{
            self.screen.push_command(command)
        }
    }

    pub fn draw_if_debug<'a>(&mut self, command: impl FnOnce() -> ScreenCommand<'a>){
        if self.show_debug{
            self.screen.push_command(command())
        }
    }

    pub fn draw_wrapping_debug(&mut self, command: ScreenCommand){
        if self.show_debug{
            self.screen.push_command_with_wrap(command)
        }
    }

    pub fn draw_wrapping_if_debug<'a>(&mut self, command: impl FnOnce() -> ScreenCommand<'a>){
        if self.show_debug{
            self.screen.push_command_with_wrap(command())
        }
    }
}


