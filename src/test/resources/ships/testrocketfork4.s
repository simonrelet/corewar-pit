.name "Ref"
.comment "Dsl j'ai pas de thibault@lse.epita.fr"

init:
        mode    assegai

        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop
        %% nop 16

        ll      r0,     384 - 6

        lc      r6,     3
        lc      r7,     -3
        lc      r9,     7

        lc      r11,    -1
        lc      r14,    -4

        ldr     r8,     [r9]
        ll      r13,    fork1 - frk1
        str     [r7],  r8

#=====================================================
#   First fork : aligned on 64, one process goes
#   forward to write the track with jumps and strs
#`````````````````````````````````````````````````````

        fork                        # must be on the 64th address
        bnz     r13                 # first ship goes to fork1 label
frk1:
        # nop nop nop nop nop nop nop # nop x7
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        # nop nop nop nop nop nop nop
        %% nop 49

        ll      r13,    startingBlock - frk2
        str     [r7],   r8
endfrk1:

#=====================================================
        fork                        # must be on the 128th address
        bz     r13
frk2:
        ll      r13,    endstart - frk2 - 12
#crash nop
        fork
        bz      r13
        mode    rocket
        ldb     [r9],   0, 39       # useless but i need time 40

        ll      r15,    sec3a - endInitSecond
        ll      r13,    initSecond - sec3a
        b       r13
sec3a:
        ll      r15,    384 - 3 + bstart - startingBlock - 3
        b       r15
#=====================================================
startingBlock:
##############
        ll      r0,     384 - 6
        mode    icaras
        # must be on the 192th address
    #nop
        b       r6          # One must give the qirex (3rd ship) proper register values
        b       r0
bstart:                 # qirex (the jump only ship) initialization
        ldb     [r15],  0,  147     # 147
        ll      r5,     startingBlock - endcritical + 13
        lc      r7,     bstart - endstart - 3
        lc      r12,    1
        ll      r1,     384 - 3
        mov     r6,     r0
        mode    qirex
        # nop nop
        %% nop 2

        b       r7
#        crash nop nop
endstart:

#=====================================================
        ll      r15,    sec4a - endInitFirst + 17
        ll      r13,    initFirst - sec4a
        b       r13
sec4a:

        ll      r11,        - 384 - 3
        ll      r0,         - 384 - 9

        ll      r14,        strt - sec4b - 384 - 5
        ll      r13,        strt - sec4b - 384 - 6

        mode rocket
        str     [r13],      r10
sec4b:
        str     [r14],      r2

        ll      r14,        - 384 - 2
        ll      r13,        strt - backward + 3 - 384 - 9
        b       r13
backward:

        # nop nop nop nop nop nop
        %% nop 6

#=====================================================
critical:
            str     [r11],     r1
            nop                     # must be on the 320th address
            check
strt:
            b       r12
            crash
#            crash
            # nop nop nop nop nop nop nop
            %% nop 7

            b       r5
endcritical:

#=====================================================

fork1:
        ll      r15,    sec6a - endInitFirst
        ll      r13,    initFirst - sec6a
        b       r13
sec6a:

        ll      r11,    384 - 3
        ll      r0,     384 - 9
        ll      r14,    bstart - endfork1 + 384 + 15
        ll      r13,    bstart - endfork1 + 384 + 14
        str     [r13],      r1
        str     [r14],      r2
        ll      r14,        384 - 2
        ll      r13,        bstart - endfork1 + 384 - 9
        mode rocket
        b       r13
endfork1:

initFirst:
        ll      r2,     0x7F3E      # 14 3 b
        ll      r3,     0x7F4E      # 14 3 b0xE4BE      # str 14 4 str
        ll      r4,     0x7F5E      # 14 3 b0xE5BE      # str 14 5 str
        ll      r5,     0x7F6E      # 14 3 b0xE6BE      # str 14 6 str
        ll      r6,     0x7F7E      # 14 3 b0xE7BE      # str 14 7 str
        ll      r7,     0x7F8E      # 14 3 b0xE8BE      # str 14 8 str
        ll      r8,     0x7F9E      # 14 3 b0xE9BE      # str 14 9 str
        ll      r9,     0x7F2E      # 14 3 b0xE1BE      # str 14 1 str
        lc      r10,    17          #the good number
        bnz      r10

        ll      r1,     0xE100
        ll      r10,    0xEABE
        b       r15

        ll      r10,    0xE1BE
        ll      r1,     0xE1BE
        b       r15
endInitFirst:
initSecond:
        ll      r2,     0x17F0
        mov     r3,     r2
        mov     r4,     r2
        mov     r5,     r2
        mov     r6,     r2
        mov     r7,     r2
        mov     r8,     r2
        ll      r9,     0x1CF1
        b       r15
endInitSecond:
