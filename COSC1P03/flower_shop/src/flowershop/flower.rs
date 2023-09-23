use super::price::Price;

#[derive(Debug, Clone, Eq, PartialEq)]
pub struct Flower {
    kind: String,
    price: Price,
}

impl Flower {
    pub fn new(name: impl ToString, price: impl Into<Price>) -> Self {
        Self {
            kind: name.to_string(),
            price: price.into(),
        }
    }

    pub fn get_price(&self) -> Price {
        self.price
    }

    pub fn name(&self) -> &str {
        &self.kind
    }
}
