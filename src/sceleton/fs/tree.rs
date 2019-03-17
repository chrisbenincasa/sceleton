use std::path::{Path, PathBuf};
use std::error::Error;
use crate::sceleton::fs::dfs::dfs_filter_cb;

pub fn process<F, Cb>(root: &Path, filter: &F, callback: &Cb) -> Vec<PathBuf>
where
    F: Fn(&PathBuf) -> bool,
    Cb: Fn(&PathBuf) -> Result<PathBuf, Box<Error>>
{
    dfs_filter_cb(root, filter, callback)
}
