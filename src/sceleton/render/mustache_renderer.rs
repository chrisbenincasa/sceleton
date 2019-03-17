use std::path::Path;

use serde::Serialize;

use crate::sceleton::render::Renderer;
use crate::sceleton::render::RenderError;
use crate::sceleton::render::Result;

pub struct Mustache {}

impl Mustache {
    pub fn new() -> Mustache {
        Mustache {}
    }
}

impl Renderer for Mustache {
    fn compile_str<T: Serialize>(&self, in_str: &str, data: T) -> Result {
        mustache::compile_str(in_str)
            .and_then(|template| template.render_to_string(&data))
            .map_err(|_| RenderError::Error)
    }

    fn compile_path<T: Serialize>(&self, in_path: &Path, data: &T) -> Result {
        mustache::compile_path(in_path)
            .and_then(|template| template.render_to_string(data))
            .map_err(|_| RenderError::Error)
    }
}

