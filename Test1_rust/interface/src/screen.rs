extern crate alloc;

use alloc::vec::Vec;

use crate::sys::{syscall_0_2, syscall_2_0, SCREEN_WIDTH_HEIGHT, SEND_MAIN_SCREEN_DRAW_CALL};

pub enum ScreenCommand<'a> {
    SetColor([u8; 4]),
    Pixel(i16, i16),
    Line(i16, i16, i16, i16),
    Clear,
    Text(&'a str, i16, i16),
    Rect(i16, i16, i16, i16),
    FilledRect(i16, i16, i16, i16),
    Polygon(&'a [(i16, i16)]),
    FilledPolygon(&'a [(i16, i16)]),
    PolygonLine(&'a [(i16, i16)]),
    Oval(i16, i16, i16, i16),
}

pub struct DrawCall {
    width: i16,
    height: i16,
    call_data: Vec<u8>,
}

impl Default for DrawCall {
    fn default() -> Self {
        Self::new()
    }
}

impl DrawCall {
    pub fn new() -> Self {
        let (width, height) = get_screen_width_height();
        Self {
            width,
            height,
            call_data: Vec::new(),
        }
    }
}

impl DrawCall {
    pub fn push_command(&mut self, command: ScreenCommand) {
        match command {
            ScreenCommand::Pixel(x, y) => {
                self.call_data.push(1);
                self.push_data(&x.to_be_bytes());
                self.push_data(&y.to_be_bytes());
            }
            ScreenCommand::Line(x1, y1, x2, y2) => {
                self.call_data.push(2);
                self.push_data(&x1.to_be_bytes());
                self.push_data(&y1.to_be_bytes());
                self.push_data(&x2.to_be_bytes());
                self.push_data(&y2.to_be_bytes());
            }
            ScreenCommand::Clear => {
                self.call_data.push(3);
            }
            ScreenCommand::Text(text, x, y) => {
                self.call_data.push(4);
                self.push_data(&x.to_be_bytes());
                self.push_data(&y.to_be_bytes());
                self.push_data(&(text.as_bytes().len() as i16).to_be_bytes());
                self.push_data(text.as_bytes());
            }
            ScreenCommand::Rect(x, y, w, h) => {
                self.call_data.push(5);
                self.push_data(&x.to_be_bytes());
                self.push_data(&y.to_be_bytes());
                self.push_data(&w.to_be_bytes());
                self.push_data(&h.to_be_bytes());
            }
            ScreenCommand::FilledRect(x, y, w, h) => {
                self.call_data.push(6);
                self.push_data(&x.to_be_bytes());
                self.push_data(&y.to_be_bytes());
                self.push_data(&w.to_be_bytes());
                self.push_data(&h.to_be_bytes());
            }
            ScreenCommand::SetColor(color) => {
                self.call_data.push(7);
                self.push_data(&color);
            }
            ScreenCommand::Polygon(points) => {
                self.call_data.push(8);
                self.push_data(&(points.len() as i16).to_be_bytes());
                for p in points {
                    self.push_data(&p.0.to_be_bytes());
                    self.push_data(&p.1.to_be_bytes());
                }
            }
            ScreenCommand::FilledPolygon(points) => {
                self.call_data.push(9);
                self.push_data(&(points.len() as i16).to_be_bytes());
                for p in points {
                    self.push_data(&p.0.to_be_bytes());
                    self.push_data(&p.1.to_be_bytes());
                }
            }
            ScreenCommand::PolygonLine(points) => {
                self.call_data.push(10);
                self.push_data(&(points.len() as i16).to_be_bytes());
                for p in points {
                    self.push_data(&p.0.to_be_bytes());
                    self.push_data(&p.1.to_be_bytes());
                }
            }
            ScreenCommand::Oval(x, y, w, h) => {
                self.call_data.push(11);
                self.push_data(&x.to_be_bytes());
                self.push_data(&y.to_be_bytes());
                self.push_data(&w.to_be_bytes());
                self.push_data(&h.to_be_bytes());
            }
        }
    }

    pub fn push_command_with_wrap(&mut self, command: ScreenCommand) {
        match command {
            ScreenCommand::Clear
            | ScreenCommand::SetColor(..)
            | ScreenCommand::Pixel(..)
            | ScreenCommand::Text(..) => self.push_command(command),
            ScreenCommand::Line(x1, y1, x2, y2) => {
                self.push_command(command);

                let mut x_off = 0;
                let mut y_off = 0;

                if x1 < 0 || x2 < 0 {
                    x_off = self.width
                }
                if x1 >= self.width || x2 >= self.width {
                    x_off = -self.width
                }
                if y1 < 0 || y2 < 0 {
                    y_off = self.height
                }
                if y1 >= self.height || y2 >= self.height {
                    y_off = -self.height
                }

                if x_off != 0 {
                    self.push_command(ScreenCommand::Line(x1 + x_off, y1, x2 + x_off, y2));
                }
                if y_off != 0 {
                    self.push_command(ScreenCommand::Line(x1, y1 + y_off, x2, y2 + y_off));
                }

                if x_off != 0 && y_off != 0 {
                    self.push_command(ScreenCommand::Line(
                        x1 + x_off,
                        y1 + y_off,
                        x2 + x_off,
                        y2 + y_off,
                    ));
                }
            }
            ScreenCommand::Rect(x, y, w, h) => {
                self.call_data.push(5);
                self.push_data(&x.to_be_bytes());
                self.push_data(&y.to_be_bytes());
                self.push_data(&w.to_be_bytes());
                self.push_data(&h.to_be_bytes());
            }
            ScreenCommand::FilledRect(x, y, w, h) => {
                self.call_data.push(6);
                self.push_data(&x.to_be_bytes());
                self.push_data(&y.to_be_bytes());
                self.push_data(&w.to_be_bytes());
                self.push_data(&h.to_be_bytes());
            }

            ScreenCommand::Polygon(points) => {
                self.call_data.push(8);
                self.wrapped_points(points);
            }
            ScreenCommand::FilledPolygon(points) => {
                self.call_data.push(9);
                self.wrapped_points(points);
            }
            ScreenCommand::PolygonLine(points) => {
                self.call_data.push(10);
                self.wrapped_points(points);
            }
            ScreenCommand::Oval(x, y, w, h) => {
                self.call_data.push(11);
                self.push_data(&x.to_be_bytes());
                self.push_data(&y.to_be_bytes());
                self.push_data(&w.to_be_bytes());
                self.push_data(&h.to_be_bytes());

                let mut x_off = 0;
                let mut y_off = 0;

                if x + w >= self.width {
                    x_off = -self.width
                }
                if x - w < 0 {
                    x_off = self.width
                }
                if y + w >= self.height {
                    y_off = -self.height
                }
                if y - w < 0 {
                    y_off = self.height
                }

                if x_off != 0 {
                    self.call_data.push(11);
                    self.push_data(&(x + x_off).to_be_bytes());
                    self.push_data(&y.to_be_bytes());
                    self.push_data(&w.to_be_bytes());
                    self.push_data(&h.to_be_bytes());
                }
                if y_off != 0 {
                    self.call_data.push(11);
                    self.push_data(&x.to_be_bytes());
                    self.push_data(&(y + y_off).to_be_bytes());
                    self.push_data(&w.to_be_bytes());
                    self.push_data(&h.to_be_bytes());
                }

                if x_off != 0 && y_off != 0 {
                    self.call_data.push(11);
                    self.push_data(&(x + x_off).to_be_bytes());
                    self.push_data(&(y + y_off).to_be_bytes());
                    self.push_data(&w.to_be_bytes());
                    self.push_data(&h.to_be_bytes());
                }
            }
        }
    }

    pub fn wrapped_points(&mut self, points: &[(i16, i16)]) {
        self.push_data(&(points.len() as i16).to_be_bytes());

        let mut x_off = 0;
        let mut y_off = 0;

        for p in points {
            self.push_data(&p.0.to_be_bytes());
            self.push_data(&p.1.to_be_bytes());
            if p.0 < 0 {
                x_off = self.width;
            }
            if p.0 >= self.width {
                x_off = -self.width;
            }
            if p.1 < 0 {
                y_off = self.height;
            }
            if p.1 >= self.height {
                y_off = -self.height;
            }
        }

        if x_off != 0 {
            self.call_data.push(8);
            self.push_data(&(points.len() as i16).to_be_bytes());
            for p in points {
                self.push_data(&(p.0 + x_off).to_be_bytes());
                self.push_data(&p.1.to_be_bytes());
            }
        }
        if y_off != 0 {
            self.call_data.push(8);
            self.push_data(&(points.len() as i16).to_be_bytes());
            for p in points {
                self.push_data(&p.0.to_be_bytes());
                self.push_data(&(p.1 + y_off).to_be_bytes());
            }
        }

        if x_off != 0 && y_off != 0 {
            self.call_data.push(8);
            self.push_data(&(points.len() as i16).to_be_bytes());
            for p in points {
                self.push_data(&(p.0 + x_off).to_be_bytes());
                self.push_data(&(p.1 + y_off).to_be_bytes());
            }
        }
    }

    pub fn get_call_len(&self) -> usize {
        self.call_data.len()
    }

    pub fn get_width(&self) -> i16 {
        self.width
    }

    pub fn get_height(&self) -> i16 {
        self.height
    }

    pub fn send_draw_call(&mut self) {
        let ptr = self.call_data.as_ptr().addr() as u32;
        let len = self.call_data.len() as u32;
        unsafe { syscall_2_0::<SEND_MAIN_SCREEN_DRAW_CALL>(ptr, len) }
        self.call_data.clear()
    }

    fn push_data(&mut self, data: &[u8]) {
        data.iter().for_each(|&d| self.call_data.push(d))
    }
}

pub fn get_screen_width_height() -> (i16, i16) {
    unsafe {
        let (width, height) = syscall_0_2::<SCREEN_WIDTH_HEIGHT>();
        (width as i16, height as i16)
    }
}
