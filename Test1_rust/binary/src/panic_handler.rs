#[panic_handler]
#[no_mangle]
fn panic(info: &core::panic::PanicInfo) -> ! {
    interface::println!("{}", info);
    interface::println!("STOPPING");
    interface::sys::halt();
}
