use std::path::Path;

pub mod mustache_renderer;

pub trait Renderer {
    fn compile_str<T: serde::Serialize>(&self, in_str: &str, data: T) -> Result;

    fn compile_path<T: serde::Serialize>(&self, in_path: &Path, data: &T) -> Result;
}

pub type Result = ::std::result::Result<String, RenderError>;

pub enum RenderError {
    Error
}
