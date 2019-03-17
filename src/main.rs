extern crate clap;
extern crate tempfile;

use clap::{App, Arg};

pub mod sceleton;

#[derive(Debug)]
pub struct SceletonConfig<'a> {
    template: &'a str,
}

impl<'a> SceletonConfig<'a> {
    pub fn new(template: &str) -> SceletonConfig {
        SceletonConfig { template }
    }
}

fn main() {
    //    let target_path = Path::new("ASD");
    //    sceleton::fs::copier::copy_dir(Path::new("asd"), target_path);
    //
    //    remove_dir_all(target_path.join(".git"));
    let matches = App::new("sceleton")
        .version("1.0")
        .arg(
            Arg::with_name("template")
                .help("The template to generate")
                .required(true)
                .index(1)
                .long_help("a big ol help"),
        )
        .arg(
            Arg::with_name("target")
                .takes_value(true)
                .help("The target diretory")
                .short("t")
                .long("target"),
        )
        .get_matches();

    let conf = SceletonConfig::new(matches.value_of("template").unwrap());

    sceleton::runner::run(&conf);
}
