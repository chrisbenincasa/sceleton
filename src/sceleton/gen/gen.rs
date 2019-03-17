use std::collections::HashMap;
use std::io;
use std::path::Path;

use mustache::MapBuilder;

use crate::sceleton::fs::dfs::dfs_filter_cb;
use crate::sceleton::fs::tree;
use crate::sceleton::render::Renderer;

pub fn gen_tree(root: &Path) {
//    dfs_filter_cb()
}

pub fn gen_files<R>(root: &Path, renderer: R)
where
    R: Renderer
{
    tree::process(root, &|buf| buf.is_file(), &|path| {
        let mut data = HashMap::new();
        data.insert(String::from("test"), String::from("VALUE"));
        renderer.compile_path(path, &data).map(|str| {
            println!("{}", str);
        });
        Ok(path.to_owned())
    });
}
