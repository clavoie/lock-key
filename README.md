lock-key
========

Symmetric encryption wrappers for Clojure.

Implements symmetric encryption in AES/CBC/PKCS5Padding mode.

## Installation

Add the following dependency to your `project.clj` file:

[![Clojars Project](http://clojars.org/lock-key/latest-version.svg)](http://clojars.org/lock-key)

## Documentation

[API](http://clavoie.github.io/lock-key/)

## Usage

```clojure
user=> (require ['lock-key.core :refer ['decrypt 'decrypt-as-str 'decrypt-from-base64
                                        'encrypt 'encrypt-as-base64]])
nil

user=> (def secret "one two three")
#'user/secret

user=> (def lock "password")
#'user/lock

user=> (def encrypted-secret (encrypt secret lock))
#'user/encrypted-secret

user=> encrypted-secret
#<byte[] [B@7ec2c524>

user=> (String. ^bytes encrypted-secret)
" ═d╙DYÄ/\\▀.)δúYªí↓\rvk═(,ô▼αæ9╞↑?"

user=> (decrypt encrypted-secret lock)
#<byte[] [B@1b3f8998>

user=> (String. (decrypt encrypted-secret lock))
"one two three"

user=> (decrypt-as-str encrypted-secret lock)
"one two three"

user=> (def encrypted-secret (encrypt-as-base64 secret lock))
#'user/encrypted-secret

user=> encrypted-secret
"UFD3vAmm5Rc3xPnXcQUJs3yO3069NtlzjJoRA2egyyo="

user=> (decrypt-from-base64 encrypted-secret lock)
"one two three"
```

