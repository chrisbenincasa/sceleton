use std::fs;
use std::path::{Path, PathBuf};

pub fn bfs(root: &Path) -> Vec<PathBuf> {
    return bfs_filter(root, &|_| true);
}

pub fn bfs_filter<T>(root: &Path, filter: &T) -> Vec<PathBuf>
where
    T: Fn(PathBuf) -> bool,
{
    let mut acc: Vec<PathBuf> = vec![];
    handle_entry(root.to_path_buf(), filter, &mut acc);
    return acc;
}

fn bfs_inner<T>(curr: &Path, filter: &T, acc: &mut Vec<PathBuf>)
where
    T: Fn(PathBuf) -> bool,
{
    if let Ok(readdir) = fs::read_dir(curr) {
        for res in readdir.into_iter() {
            if let Ok(entry) = res {
                if !acc.contains(&entry.path()) && filter(entry.path()) {
                    handle_entry(entry.path(), filter, acc);
                }
            }
        }
    };
}

fn handle_entry<T>(entry: PathBuf, filter: &T, acc: &mut Vec<PathBuf>)
where
    T: Fn(PathBuf) -> bool,
{
    let is_dir = entry.is_dir();

    // Capture the next path before we give ownership of the entry to the vector
    let next_path = entry.as_path().to_owned();

    // Push the new entry
    acc.push(entry);

    if is_dir {
        // Recurse if this entry is a directory
        bfs_inner(next_path.as_path(), filter, acc);
    }
}
