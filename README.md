lock-key
========

Symmetric encryption wrappers for Clojure.

Implements symmetric encryption in AES/CBC/PKCS5Padding mode.

**Note to users of v1.0.0**

v1.1.0 of lock-key changes the encryption algorithm used by v1.0.0. If you have any data encrypted with v1.0.0 the v1.1.0 lock-key.core fns will not be able to decrypt it. The v1.0.0 fns have been moved into the `lock-key.aes` namespace if you wish to use them alongside v1.1.0, and will be removed in a future release.

## Installation

Add the following dependency to your `project.clj` file:

[![Clojars Project](http://clojars.org/lock-key/latest-version.svg)](http://clojars.org/lock-key)

## Usage

```clojure
user=> (require ['lock-key.core :refer ['decrypt 'decrypt-as-str 'encrypt]])
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
```

## Documentation

[API](http://clavoie.github.io/lock-key/)

## License

Copyright © 2014 Chris LaVoie

Distributed under the Eclipse Public License, the same as Clojure.

