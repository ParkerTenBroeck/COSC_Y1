use super::price::Price;

#[derive(Debug, Clone, Eq, PartialEq)]
pub struct Vause {
    kind: String,
    price: Price,
}

impl Vause {
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

impl PartialOrd for Vause {
    fn partial_cmp(&self, other: &Self) -> Option<std::cmp::Ordering> {
        self.price.partial_cmp(&other.price)
    }
}

impl Ord for Vause {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
        self.price.cmp(&other.price)
    }
}
