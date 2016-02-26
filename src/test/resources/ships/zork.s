    .name "Zork"
    .comment "This is the first ship ever."

    ll r0, 0x2ecf
    ll r1, 0x13e0
    ll r2, to - from1
    ll r3, to - from2 + 4
    str [r2], r0
from1:
    str [r3], r1
from2:
to: