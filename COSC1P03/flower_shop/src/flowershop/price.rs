#[derive(Default, Debug, Copy, Clone, PartialEq, Eq, PartialOrd, Ord)]
pub struct Price {
    cents: i64,
}

impl ToString for Price {
    fn to_string(&self) -> String {
        format!("${}.{}", self.cents / 100, (self.cents % 100).abs())
    }
}

impl From<f32> for Price {
    fn from(value: f32) -> Self {
        Self {
            cents: (value * 100.0) as i64,
        }
    }
}

impl core::ops::Add for Price {
    type Output = Price;

    fn add(self, rhs: Self) -> Self::Output {
        Self {
            cents: self.cents + rhs.cents,
        }
    }
}

impl core::ops::AddAssign for Price {
    fn add_assign(&mut self, rhs: Self) {
        self.cents += rhs.cents;
    }
}

impl Price {
    pub fn bruh(&self) {}
}
