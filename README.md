sceleton
===

[![Build Status](https://travis-ci.org/chrisbenincasa/sceleton.svg?branch=master)](https://travis-ci.org/chrisbenincasa/sceleton)

Sceleton is a simple tool for scaffolding directories and files using user-inputted data.

## Running Sceleton
Sceleton can be run using a variety of methods:

#### As a docker container
Coming soon!

#### As a standalone program
Coming soon!

#### As an SBT plugin
Coming soon!

#### Within a JVM-based program
Lastly, you can use Sceleton directly by taking a dependency on `sceleton-core` and calling the runner manually:

```scala
import com.chrisbenincasa.tools.sceleton.SceletonOptions
import com.chrisbenincasa.tools.sceleton.core.SceletonRunner

val config = 
    SceletonOptions(
        "user/github-repo", 
        new java.io.File("output"), 
        prefilledProps = Map("name" -> "test service")
    )

new SceletonRunner(config).run() match {
    case Success(_) => 
    case Failure(ex) => 
}
```

## Writing templates
Write templates using any of the supported templating languages. File names are also considered when applying templates.

When given a directory, Sceleton will search for the deepest directory that ends with the word `sceleton`. This is treated as the scaffold root; nothing above
the root is transformed and only files/directories under the root are outputted to the target directory.

#### Supported template sources:
* Local filesystem
* Github (or any git repository)

#### Supported template engines:
* Mustache  

## Transforms
There is support for textual transforms on templated values. Transforms can also be chained together.

Simple transform to lowercase:
```
{{ name;format="lower" }}
```

Chained transforms:
```
# transforms "Bob Smith" to "bob_smith"
{{ name;format="lower,underscore" }}
```

#### Available transforms
* `camel` - Converts a string to "camelCase"
* `Camel` - Converts a string to "CamelCase/PascalCase"
* `start`|`start-case` - Capitalizes the first letter in each word
*  `underscore`|`snake`|`snake-case` - Converts a string to snake_case (replaces dots and spaces with underscores)
* `hyphenate`|`hyphen` - Replaces spaces with hypens
* `normalize`|`norm` - Compound transform: runs `lower-case` and then `hyphenate` on a string
* `packaged`|`package-dir` - Replaces dots with slashes; useful for generating directory trees
* `upper`|`upper-case` - Converts an entire string to uppercase
* `lower`|`lower-case` - Converts an entire string to lowercase
* `cap`|`capitalize` - Capitalizes the first letter of a string
* `decap`|`decapitalize` - Un-capitalizes the first letter of a string 
* `word`|`word-only` - Removes all non-word characters in a string
* `random`|`generate-random` - Generates a random alphanumeric string of length 10

#### Defining variables
Variables for a scaffold are defined in a file entitled `default.properties` in the template root. This file uses the standard format for a [properties file](https://en.wikipedia.org/wiki/.properties). The special key `description` can be used to print a message to the user during scaffold time.

Example:

```
description=Scaffold a new microservice!
name=
organization=com.chrisbenincasa.services
```

## Planned features
* Run options
    - [ ] SBT Plugin
    - [ ] Docker container for running with no required Java setup
    - [ ] Standalone app (Assembly JAR)
* Scaffold options
    - [ ] Change mustache delimiter
    - [ ] Change transform delimiter
    - [ ] Support pluggable renderer (engines other than Mustache)
    - [ ] Support pluggable transforms (would only be available for code-based executions, e.g. SBT, direct)
