use super::{flower::Flower, price::Price, vause::Vause};

#[derive(Debug, Clone)]
pub struct Arrangement {
    vause: Vause,
    flowers: Vec<Flower>,
}

impl Arrangement {
    pub fn new(vause: Vause, flowers: Vec<Flower>) -> Self {
        Self { vause, flowers }
    }

    pub fn vause(&self) -> &Vause {
        &self.vause
    }

    pub fn flowers(&self) -> &[Flower] {
        &self.flowers
    }

    pub fn get_price(&self) -> Price {
        let mut sum = Price::default();
        for price in self.flowers.iter().map(Flower::get_price) {
            sum += price;
        }
        self.vause.get_price() + sum
    }

    pub fn compare_price(&self, rhs: &Self) -> std::cmp::Ordering {
        self.get_price().cmp(&rhs.get_price())
    }
}
