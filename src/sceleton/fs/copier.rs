use self::fs_extra::dir::CopyOptions;
use std::fs::copy;
use std::path::Path;

extern crate fs_extra;

pub fn copy_dir(src: &Path, dest: &Path) -> fs_extra::error::Result<u64> {
    println!(
        "Copying {} to {}",
        src.to_str().unwrap(),
        dest.to_str().unwrap()
    );

    fs_extra::dir::copy(src, dest, &CopyOptions::new())
}
