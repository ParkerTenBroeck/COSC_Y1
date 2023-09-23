pub mod arrangement;
pub mod customer_order;
pub mod flower;
pub mod price;
pub mod vause;

use std::{cmp::Ordering, error::Error};

use self::{arrangement::Arrangement, customer_order::CustomerOrder, flower::Flower, vause::Vause};

#[derive(Debug, Default, Clone)]
pub struct FlowerShop {
    flower_catalog: Vec<Flower>,
    vause_catalog: Vec<Vause>,
    customer_orders: Vec<CustomerOrder>,
}

impl FlowerShop {
    pub fn add_flower(&mut self, flower: Flower) {
        self.flower_catalog.push(flower);
    }

    pub fn add_vause(&mut self, vause: Vause) {
        self.vause_catalog.push(vause);
    }

    pub fn add_order(&mut self, order: CustomerOrder) {
        for (index, item) in self.customer_orders.iter().enumerate() {
            match order.cmp_priority(item) {
                Ordering::Greater | Ordering::Equal => {
                    self.customer_orders.insert(index, order);
                    return;
                }
                Ordering::Less => {}
            }
        }

        self.customer_orders.push(order);
    }

    pub fn next_order(&mut self) -> Option<CustomerOrder> {
        self.customer_orders.pop()
    }

    pub fn create_arragnement(
        &self,
        vause: &str,
        flowers: Vec<&str>,
    ) -> Result<Arrangement, Box<dyn Error>> {
        let vause = self.vause_catalog.iter().find(|i| i.name() == vause);
        let vause = match vause {
            Some(vause) => vause,
            None => Err("Vause Not in cataloge")?,
        };
        let mut actual_flowers = Vec::new();

        for flower in flowers {
            let f = self.flower_catalog.iter().find(|i| i.name() == flower);
            let f = match f {
                Some(f) => f,
                None => Err(format!("Flower \"{}\" Not in cataloge", flower))?,
            };
            actual_flowers.push(f.to_owned());
        }

        Ok(Arrangement::new(vause.to_owned(), actual_flowers))
    }

    pub fn vauses(&self) -> &[Vause] {
        &self.vause_catalog
    }

    pub fn flowers(&self) -> &[Flower] {
        &self.flower_catalog
    }
}
