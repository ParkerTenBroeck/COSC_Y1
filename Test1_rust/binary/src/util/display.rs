#[allow(dead_code)]
#[derive(Copy, Clone, Debug)]
pub struct Color([u8; 4]);

impl Color {
    #[inline(always)]
    pub const fn from_rgb(r: u8, g: u8, b: u8) -> Self {
        Self([r, g, b, 255])
    }
    #[inline(always)]
    pub const fn from_rgba(r: u8, g: u8, b: u8, a: u8) -> Self {
        Self([r, g, b, a])
    }

    #[inline(always)]
    pub const fn from_rgb_additive(r: u8, g: u8, b: u8) -> Self {
        Self([r, g, b, 0])
    }

    #[inline(always)]
    pub const fn clear() -> Self {
        Self([0, 0, 0, 0])
    }

    pub fn get_inner(&self) -> &[u8; 4] {
        &self.0
    }

    #[inline(always)]
    pub fn is_opaque(&self) -> bool {
        self.0[3] == 255
    }
    #[inline(always)]
    pub fn get_alpha(&self) -> u8 {
        self.0[3]
    }
}

impl From<Color> for u32 {
    fn from(color: Color) -> Self {
        (color.0[0] as u32) | ((color.0[1] as u32) << 8) | ((color.0[2] as u32) << 16)
    }
}

impl From<u32> for Color {
    fn from(color: u32) -> Self {
        Color::from_rgb(
            (color & 255) as u8,
            ((color >> 8) & 255) as u8,
            ((color >> 16) & 255) as u8,
        )
    }
}
