use std::error::Error;
use std::fs;
use std::path::{Path, PathBuf};

pub fn dfs(root: &Path) -> Vec<PathBuf> {
    return dfs_filter(root, &|_| true);
}

pub fn dfs_filter<F>(root: &Path, filter: &F) -> Vec<PathBuf>
where
    F: Fn(&PathBuf) -> bool,
{
    dfs_filter_cb(root, filter, &|pb| Ok(pb.to_owned()))
}

pub fn dfs_filter_cb<F, Cb>(root: &Path, filter: &F, callback: &Cb) -> Vec<PathBuf>
where
    F: Fn(&PathBuf) -> bool,
    Cb: Fn(&PathBuf) -> Result<PathBuf, Box<Error>>,
{
    let mut acc: Vec<PathBuf> = vec![];

    let path_buf = root.to_path_buf();

    handle_entry(path_buf, filter, callback, &mut acc);

    return acc;
}

fn dfs_inner<F, Cb>(curr: &Path, filter: &F, callback: &Cb, acc: &mut Vec<PathBuf>)
where
    F: Fn(&PathBuf) -> bool,
    Cb: Fn(&PathBuf) -> Result<PathBuf, Box<Error>>,
{
    if let Ok(readdir) = fs::read_dir(curr) {
        for res in readdir.into_iter() {
            if let Ok(entry) = res {
                if !acc.contains(&entry.path()) {
                    handle_entry(entry.path(), filter, callback, acc);
                }
            }
        }
    };
}

fn handle_entry<F, Cb>(entry: PathBuf, filter: &F, callback: &Cb, acc: &mut Vec<PathBuf>)
where
    F: Fn(&PathBuf) -> bool,
    Cb: Fn(&PathBuf) -> Result<PathBuf, Box<Error>>,
{
    match callback(&entry) {
        Ok(_) => (),
        Err(x) => ()
    }

    let is_dir = entry.is_dir();

    if is_dir {
        dfs_inner(entry.as_path(), filter, callback, acc);
    }

    if filter(&entry) {
        acc.push(entry);
    }
}
