lock-key
========

Symmetric encryption wrappers for Clojure

## Installation

Add the following dependency to your `project.clj` file:

```
[lock-key "1.0.0"]
```

## Usage

```clojure
user=> (require ['lock-key.core :refer ['decrypt 'encrypt]])
nil
user=> (def secret "one two three")
#'user/secret
user=> (def lock "password")
#'user/lock
user=> (def secret (encrypt "one two three" lock))
#'user/secret
user=> secret
#<byte[] [B@47bd871c>
user=> (decrypt secret lock)
#<byte[] [B@63017675>
user=> (String. (decrypt secret lock))
"one two three"
```

## Documentation

[API](http://clavoie.github.io/lock-key/)

## License

Copyright © 2014 Chris LaVoie

Distributed under the Eclipse Public License, the same as Clojure.

