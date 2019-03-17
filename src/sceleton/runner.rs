use crate::{sceleton, SceletonConfig};
use std::fs::create_dir;
use std::path::{Path, PathBuf};
use std::time::{SystemTime, UNIX_EPOCH};
use url::{Host, Url};
use crate::sceleton::gen::gen::{gen_tree, gen_files};

pub fn run(config: &SceletonConfig) {
    println!("Running with config: {:?}", config);

    let path_opt = if let Ok(parsed) = Url::parse(config.template) {
        match parsed.scheme() {
            "file" => {
                let path = match parsed.host() {
                    // Hack to support relative paths
                    Some(Host::Domain(rel)) => format!("{}{}", rel.to_owned(), parsed.path()),
                    Some(host) => panic!("Host {:?} is invalid for scheme file", host),
                    // If we have scheme == file and no host, it's an absolute path
                    None => parsed.path().to_owned(),
                };

                Some(Path::new(&path).to_owned())
            }
            "git" => {
                println!("Got a git url");
                None
            }
            x => {
                println!("Unsupported scheme: {}", x);
                None
            }
        }
    } else {
        println!("We may have a repo! {}", config.template);
        None
    };

    if let Some(path) = path_opt {
        let working_path = create_working_dir(&path);

        println!("Created working dir at {}", working_path.to_str().unwrap());

        if let Some(root) = sceleton::gen::template_root::identify(&working_path) {
            gen_files(&root, sceleton::render::mustache_renderer::Mustache::new())
        }
    }
}

fn create_working_dir(template: &str) -> Option<PathBuf> {
    let start = SystemTime::now();
    let since_the_epoch = start.duration_since(UNIX_EPOCH).unwrap().as_millis();

    let dest = &::std::env::temp_dir().join(format!("sceleton_{}", since_the_epoch));

    match get_template_type(template) {
        Some(TemplateType::LocalFile(path)) => {
            match create_dir(dest) {
                Ok(_) => {
                    sceleton::fs::copier::copy_dir(src, dest);
                    return Some(dest.to_owned());
                }
                Err(e) => panic!(""),
            }
        },
        _ => None
    }
}

fn get_template_type(template: &str) -> Option<TemplateType> {
    if let Ok(parsed) = Url::parse(config.template) {
        match parsed.scheme() {
            "file" => {
                let path = match parsed.host() {
                    // Hack to support relative paths
                    Some(Host::Domain(rel)) => format!("{}{}", rel.to_owned(), parsed.path()),
                    Some(host) => panic!("Host {:?} is invalid for scheme file", host),
                    // If we have scheme == file and no host, it's an absolute path
                    None => parsed.path().to_owned(),
                };

                Some(TemplateType::LocalFile(Path::new(&path).to_owned()))
            }
            "git" => {
                println!("Got a git url");
                None
            }
            x => {
                println!("Unsupported scheme: {}", x);
                None
            }
        }
    } else {
        println!("We may have a repo! {}", config.template);
        None
    }
}

enum TemplateType {
    LocalFile(PathBuf),
    GithubRepo(String, String)
}
