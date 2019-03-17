use crate::sceleton::fs::dfs::dfs_filter;
use std::path::{Path, PathBuf};

/// Identifies the root of the template via DFS
pub fn identify(path: &Path) -> Option<PathBuf> {
    let result = dfs_filter(path, &|path| path.is_dir() && path.ends_with("sceleton"));

    if result.is_empty() {
        return Some(path.to_path_buf());
    } else {
        return result.first().map(|path| path.to_path_buf());
    }
}
