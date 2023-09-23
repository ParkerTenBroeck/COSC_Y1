use std::cmp::Ordering;

use super::arrangement::Arrangement;

#[derive(Debug, Clone)]
pub struct CustomerOrder {
    customer: String,
    rushed: bool,
    arrangement: Arrangement,
}

impl CustomerOrder {
    pub fn new(customer: String, rushed: bool, arrangement: Arrangement) -> Self {
        Self {
            customer,
            rushed,
            arrangement,
        }
    }

    pub fn customer(&self) -> &str {
        &self.customer
    }

    pub fn was_rushed(&self) -> bool {
        self.rushed
    }

    pub fn arrangement(&self) -> &Arrangement {
        &self.arrangement
    }

    pub fn cmp_priority(&self, rhs: &Self) -> Ordering {
        match (self.rushed, rhs.rushed) {
            (true, false) => Ordering::Greater,
            (false, true) => Ordering::Less,
            _ => self
                .arrangement
                .get_price()
                .cmp(&rhs.arrangement.get_price()),
        }
    }
}
