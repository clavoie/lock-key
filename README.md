lock-key
========

Symmetric encryption wrappers for Clojure.

Implements symmetric encryption in AES/CBC/PKCS5Padding mode.

## Installation

Add the following dependency to your `project.clj` file:

[![Clojars Project](http://clojars.org/lock-key/latest-version.svg)](http://clojars.org/lock-key)

## Usage

```clojure
user=> (require ['lock-key.core :refer ['decrypt 'decrypt-as-str 'encrypt 'encrypt-as-websafe 'decrypt-from-websafe]])
nil

user=> (def secret "one two three")
#'user/secret

user=> (def lock "password")
#'user/lock

user=> (def secret (encrypt "one two three" lock))
#'user/secret

user=> secret
#<byte[] [B@7ec2c524>

user=> (String. ^bytes secret)
" ═d╙DYÄ/\\▀.)δúYªí↓\rvk═(,ô▼αæ9╞↑?"

user=> (decrypt secret lock)
#<byte[] [B@1b3f8998>

user=> (String. (decrypt secret lock))
"one two three"

user=> (decrypt-as-str secret lock)
"one two three"

user=> (def secret (encrypt-as-websafe "one two three" lock))
#'user/secret

user=> secret
"UFD3vAmm5Rc3xPnXcQUJs3yO3069NtlzjJoRA2egyyo="

user=> (decrypt-from-websafe secret lock)
"one two three"
```

## Documentation

[API](http://clavoie.github.io/lock-key/)

## License

Copyright © 2014-2015 Chris LaVoie

Distributed under the Eclipse Public License, the same as Clojure.

