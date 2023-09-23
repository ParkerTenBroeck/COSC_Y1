use std::error::Error;

use flowershop::{
    arrangement::Arrangement, customer_order::CustomerOrder, flower::Flower, vause::Vause,
    FlowerShop,
};

pub mod flowershop;
// use eframe::egui;

pub fn main() {
    let mut line = String::new();

    let mut shop = flowershop::FlowerShop::default();
    shop.add_flower(Flower::new("Poppy", 12.2));
    shop.add_flower(Flower::new("Tulip", 15.50));
    shop.add_flower(Flower::new("Bruh:3", 420.64));

    shop.add_vause(Vause::new("Basic", 10.00));
    shop.add_vause(Vause::new("Fancy", 25.69));

    println!("Vauses");
    for vause in shop.vauses() {
        println!("  {}: {}", vause.name(), vause.get_price().to_string());
    }
    println!("Flowers");
    for flower in shop.flowers() {
        println!("  {}: {}", flower.name(), flower.get_price().to_string());
    }

    loop {
        println!("Enter your order:<name>[!]? <vause> <flower>+");
        _ = std::io::stdin().read_line(&mut line).unwrap();
        let order = match make_order_from_str(&shop, &line) {
            Ok(order) => order,
            Err(_) => {
                println!("Invalid input >:(");
                continue;
            }
        };
        line.clear();

        println!("Added order");
        println!("Vause: {}", order.arrangement().vause().name());
        println!("Flowers");
        for flower in order.arrangement().flowers() {
            println!("  {}", flower.name());
        }

        println!(
            "Total Price: {}",
            order.arrangement().get_price().to_string()
        );

        shop.add_order(order);
    }
}

pub fn make_order_from_str(shop: &FlowerShop, str: &str) -> Result<CustomerOrder, Box<dyn Error>> {
    let mut tokens: Vec<&str> = str
        .split(|c: char| c.is_whitespace())
        .map(|i| i.trim())
        .filter(|i| !i.is_empty())
        .rev()
        .collect();
    if tokens.len() < 3 {
        Err("Not enough arguments")?
    }
    let mut customer = tokens.pop().unwrap().to_owned();
    let rushed = customer.ends_with('!');
    if rushed {
        _ = customer.pop(); //remove bang at end
    }

    let vause = tokens.pop().unwrap();
    let flowers: Vec<&str> = tokens;

    Ok(CustomerOrder::new(
        customer,
        rushed,
        shop.create_arragnement(vause, flowers)?,
    ))
}

// fn main() -> Result<(), eframe::Error> {

//     let options = eframe::NativeOptions {
//         initial_window_size: Some(egui::vec2(320.0, 240.0)),
//         ..Default::default()
//     };
//     eframe::run_native(
//         "My egui App",
//         options,
//         Box::new(|_cc| Box::new(MyApp::default())),
//     )
// }

// struct MyApp {
//     name: String,
//     age: u32,
// }

// impl Default for MyApp {
//     fn default() -> Self {
//         Self {
//             name: "Arthur".to_owned(),
//             age: 42,
//         }
//     }
// }

// impl eframe::App for MyApp {
//     fn update(&mut self, ctx: &egui::Context, _frame: &mut eframe::Frame) {
//         egui::CentralPanel::default().show(ctx, |ui| {
//             ui.heading("My egui Application");
//             ui.horizontal(|ui| {
//                 let name_label = ui.label("Your name: ");
//                 ui.text_edit_singleline(&mut self.name)
//                     .labelled_by(name_label.id);
//             });
//             ui.add(egui::Slider::new(&mut self.age, 0..=120).text("age"));
//             if ui.button("Click each year").clicked() {
//                 self.age += 1;
//             }
//             ui.label(format!("Hello '{}', age {}", self.name, self.age));
//         });
//     }
// }
