.name "Ref"
.comment "Dsl j'ai pas de thibault@lse.epita.fr"

init:
        mode    assegai                     # 0 + 3

        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop                 # 3 + 16

        ll      r0,     384 - 6             # 19 + 7

        lc      r6,     3                   # 26 + 5
        lc      r7,     -3                  # 31 + 5
        lc      r9,     7                   # 36 + 5

        lc      r11,    -1                  # 41 + 5
        lc      r14,    -4                  # 46 + 5

        ldr     r8,     [r9]                # 51 + 3
        ll      r13,    fork1 - frk1        # 54 + 7
        str     [r7],  r8                   # 61 + 3

#=====================================================
#   First fork : aligned on 64, one process goes
#   forward to write the track with jumps and strs
#`````````````````````````````````````````````````````

        fork                        # must be on the 64th address # 64 + 2
        bnz     r13                 # first ship goes to fork1 label # 66 + 3
frk1:
        # nop nop nop nop nop nop nop # nop x7
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop     # 69 + 49

        ll      r13,    startingBlock - frk2 # 118 + 7
        str     [r7],   r8                  # 125 + 3
endfrk1:

#=====================================================
        fork                        # must be on the 128th address # 128 + 2
        bz     r13                          # 130 + 3
frk2:
        ll      r13,    endstart - frk2 - 12 # 133 + 7
#crash nop
        fork                                # 140 + 2
        bz      r13                         # 142 + 3
        mode    rocket                      # 145 + 3
        ldb     [r9],   0, 39       # useless but i need time 40 # 148 + 7

        ll      r15,    sec3a - endInitSecond # 155 + 7
        ll      r13,    initSecond - sec3a  # 162 + 7
        b       r13                         # 169 + 3
sec3a:
        ll      r15,    384 - 3 + bstart - startingBlock - 3 # 172 + 7
        b       r15                         # 179 + 3
#=====================================================
startingBlock:
##############
        ll      r0,     384 - 6             # 182 + 7
        mode    icaras                      # 189 + 3
        # must be on the 192th address
    #nop
        b       r6          # One must give the qirex (3rd ship) proper register values # 192 + 3
        b       r0                          # 195 + 3
bstart:                 # qirex (the jump only ship) initialization
        ldb     [r15],  0,  147     # 147   # 198 + 7
        ll      r5,     startingBlock - endcritical + 13 # 205 + 7
        lc      r7,     bstart - endstart - 3 # 212 + 5
        lc      r12,    1                   # 217 + 5
        ll      r1,     384 - 3             # 222 + 7
        mov     r6,     r0                  # 229 + 3
        mode    qirex                       # 232 + 3
        # nop nop
    nop nop                                 # 235 + 2

        b       r7                          # 237 + 3
#        crash nop nop
endstart:

#=====================================================
        ll      r15,    sec4a - endInitFirst + 17 # 240 + 7
        ll      r13,    initFirst - sec4a   # 247 + 7
        b       r13                         # 254 + 3
sec4a:

        ll      r11,        - 384 - 3       # 257 + 7
        ll      r0,         - 384 - 9       # 264 + 7

        ll      r14,        strt - sec4b - 384 - 5 # 271 + 7
        ll      r13,        strt - sec4b - 384 - 6 # 278 + 7

        mode rocket                         # 285 + 3
        str     [r13],      r10             # 288 + 3
sec4b:
        str     [r14],      r2              # 291 + 3

        ll      r14,        - 384 - 2       # 294 + 7
        ll      r13,        strt - backward + 3 - 384 - 9 # 301 + 7
        b       r13                         # 308 + 3
backward:

        # nop nop nop nop nop nop
    nop nop nop nop nop nop                 # 311 + 6

#=====================================================
critical:
            str     [r11],     r1           # 317 + 3
            nop                     # must be on the 320th address # 320 + 1
            check                           # 321 + 2
strt:
            b       r12                     # 323 + 3
            crash                           # 326 + 1
#            crash
            # nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop             # 327 + 7

            b       r5                      # 334 + 3
endcritical:

#=====================================================

fork1:
        ll      r15,    sec6a - endInitFirst # 337 + 7
        ll      r13,    initFirst - sec6a   # 344 + 7
        b       r13                         # 351 + 3
sec6a:

        ll      r11,    384 - 3             # 354 + 7
        ll      r0,     384 - 9             # 361 + 7
        ll      r14,    bstart - endfork1 + 384 + 15 # 368 + 7
        ll      r13,    bstart - endfork1 + 384 + 14 # 375 + 7
        str     [r13],      r1              # 382 + 3
        str     [r14],      r2              # 385 + 3
        ll      r14,        384 - 2         # 388 + 7
        ll      r13,        bstart - endfork1 + 384 - 9 # 395 + 7
        mode rocket                         # 402 + 3
        b       r13                         # 405 + 3
endfork1:

initFirst:
        ll      r2,     0x7F3E      # 14 3 b # 408 + 7
        ll      r3,     0x7F4E      # 14 3 b0xE4BE      # str 14 4 str # 415 + 7
        ll      r4,     0x7F5E      # 14 3 b0xE5BE      # str 14 5 str # 422 + 7
        ll      r5,     0x7F6E      # 14 3 b0xE6BE      # str 14 6 str # 429 + 7
        ll      r6,     0x7F7E      # 14 3 b0xE7BE      # str 14 7 str # 436 + 7
        ll      r7,     0x7F8E      # 14 3 b0xE8BE      # str 14 8 str # 443 + 7
        ll      r8,     0x7F9E      # 14 3 b0xE9BE      # str 14 9 str # 450 + 7
        ll      r9,     0x7F2E      # 14 3 b0xE1BE      # str 14 1 str # 457 + 7
        lc      r10,    17          #the good number # 464 + 5
        bnz      r10                        # 469 + 3

        ll      r1,     0xE100              # 472 + 7
        ll      r10,    0xEABE              # 479 + 7
        b       r15                         # 486 + 3

        ll      r10,    0xE1BE              # 489 + 7
        ll      r1,     0xE1BE              # 496 + 7
        b       r15                         # 503 + 3
endInitFirst:
initSecond:
        ll      r2,     0x17F0              # 506 + 7
        mov     r3,     r2                  # 513 + 3
        mov     r4,     r2                  # 516 + 3
        mov     r5,     r2                  # 519 + 3
        mov     r6,     r2                  # 522 + 3
        mov     r7,     r2                  # 525 + 3
        mov     r8,     r2                  # 528 + 3
        ll      r9,     0x1CF1              # 531 + 7
        b       r15                         # 538 + 3
endInitSecond:
