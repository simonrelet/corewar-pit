    .name "Zork"
    .comment "This is the first ship ever."

#
#  The Ship
# ----------
#
# Version: 2.1
#
# Authors: Simon Relet (relet_s)
#          Thibault Hartmann (hartma_t)
#
# March, 2013
#

#
#  Structure
# -----------
#
#  address:  65472  0   64   128  192  256  320  384  448  512  576  640  704  768  832  896  960
#              |    |    |    |    |    |    |    |    |    |    |    |    |    |    |    |    |
#  jumps *:    *    |    |    |    |    |    *    |    |    |    *    |    |    |    |    |    *
#  blocs:      |    |  [f1]  [fback ][back][stb][f2] [f3] [f4] [stf][ for][ ffor ] [f5][S]|    |[lc]
#

#
#  First Wave
# ------------
#
#  Backward:
#   str       | str         |  b
#   [e][2][4] | [e][3][5-f] | [f][7]
#
#  Forward:
#   str       | str            |  b
#   [e][2][4] | [e][3][5-bd-f] | [f][7]
#

#
#  Second Wave
# -------------
#
#  Backward & Forward:
#             |  b     4  |  b
#   [.][.][.] | [f][7][4] | [f][.]
#
#             | check;nop |  b
#   [.][.][.] | [f][c][1] | [f][.]
#

#
#  Visibility
# ------------
#
#  Mode Rocket
#    Big jumps:   6 * 64 = 384
#    Small jump:  4 * 64 = 256
#


    mode    assegai                         # 0 + 3

    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop     # 3 + 39


#
#  Fork 1
# --------
#

f1_s:
    ll      r0,     f2_s - f1_e             # 42 + 7
    lc      r2,     -3                      # 49 + 5
    ll      r3,     0xfe7e                  # 54 + 7
    str     [r2],   r3                      # 61 + 3
    # Aligned on 1 * 64
    fork                                    # 64 + 2
    bz      r0                              # 66 + 3

f1_e:
    ll      r0,     back_s - f1_help        # 69 + 7
    b       r0                              # 76 + 3
f1_help:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop                                 # 79 + 32


#
#  Init fast backward
# --------------------
#
#  registers:
#     r0:  dst jmp
#     r1:  dst jmp (= r0, needed by Starter)
#     r2:
#     r3:  dst write instr
#     r4:
#     r5:  instr 2: 'check; nop; b'
#     r6:  instr 1: 'b r4; b'
#     r7:  instr 1
#     r8:  instr 1
#     r9:  instr 1
#     r10: instr 1
#     r11: instr 1
#     r12: instr 1
#     r13: instr 1
#     r14: instr 1
#     r15: instr 1
#

fback_s:
    ll      r0,     -384 - 6                # 111 + 7
    or      r1,     r0                      # 118 + 3
    lc      r3,     -3                      # 121 + 5

    ll      r5,     0xf1cf                  # 126 + 7
    ll      r6,     0xf47f                  # 133 + 7
    mov     r7,     r6                      # 140 + 3
    mov     r8,     r6                      # 143 + 3
    mov     r9,     r6                      # 146 + 3
    mov     r10,    r6                      # 149 + 3
    mov     r11,    r6                      # 152 + 3
    mov     r12,    r6                      # 155 + 3
    mov     r13,    r6                      # 158 + 3
    mov     r14,    r6                      # 161 + 3
    mov     r15,    r6                      # 164 + 3

    # Timing
    ldb     [r0],   0,      70              # 167 + 7
    asr     r0,     0                       # 174 + 3
    # write   r0

    nop nop nop nop nop nop nop nop         # 177 + 8


    ll      r2,     st_b_s - fback_e + 6    # 185 + 7
    mode    rocket                          # 192 + 3
    b       r2                              # 195 + 3
fback_e:


#
#  Init backward
# ---------------
#
#  registers:
#     r0:  dst jmp
#     r1:  dst jmp (= r0, needed by Starter)
#     r2:  dst write instr 1
#     r3:  dst write instr 2
#     r4:  instr 1 (commun): 'str [r2] r4; str'
#     r5:  instr 2: '[r3] r6; b ( r0 )'
#     r6:  instr 2: '[r3] r5; b ( r0 )'
#     r7:  instr 2: ...
#     r8:  instr 2
#     r9:  instr 2
#     r10: instr 2
#     r11: instr 2
#     r12: instr 2
#     r13: instr 2
#     r14: instr 2
#     r15: instr 2
#

back_s:
    mode    auricom                         # 198 + 3
    ll      r0,     -384 - 9                # 201 + 7
    or      r1,     r0                      # 208 + 3
    ll      r2,     -384 - 3                # 211 + 7
    ll      r3,     -384 - 2                # 218 + 7

    ll      r4,     0xe42e                  # 225 + 7
    ll      r5,     0x7ff3                  # 232 + 7
    ll      r6,     0x7f53                  # 239 + 7
    ll      r7,     0x7f63                  # 246 + 7
    ll      r8,     0x7f73                  # 253 + 7
    ll      r9,     0x7f83                  # 260 + 7
    ll      r10,    0x7f93                  # 267 + 7
    ll      r11,    0x7fa3                  # 274 + 7
    ll      r12,    0x7fb3                  # 281 + 7
    ll      r13,    0x7fc3                  # 288 + 7
    ll      r14,    0x7fd3                  # 295 + 7
    ll      r15,    0x7fe3                  # 302 + 7

    # Timing
    # mov     r0,     r0
    # write   r0
    # or      r0,     r0


    nop nop nop nop nop                     # 309 + 5


back_e:


#
#  Starter backward
# ------------------
#

st_b_s:
    mode    rocket                          # 314 + 3
    str     [r2],   r4                      # 317 + 3
    # Align on 5 * 64
    str     [r3],   r5                      # 320 + 3
    b       r1                              # 323 + 3
st_b_e:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop                 # 326 + 36


#
#  Fork 2
# --------
#

f2_s:
    ll      r0,     f3_s - f2_e             # 362 + 7
    lc      r2,     -3                      # 369 + 5
    ll      r3,     0xfe7e                  # 374 + 7
    str     [r2],   r3                      # 381 + 3
    # Aligned on 6 * 64
    fork                                    # 384 + 2
    bz      r0                              # 386 + 3
f2_e:

    ll      r0,     for_s - f2_help         # 389 + 7
    b       r0                              # 396 + 3
f2_help:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop             # 399 + 27


#
#  Fork 3
# --------
#

f3_s:
    ll      r0,     f4_s - f3_e             # 426 + 7
    lc      r2,     -3                      # 433 + 5
    ll      r3,     0xfe7e                  # 438 + 7
    str     [r2],   r3                      # 445 + 3
    # Aligned on 7 * 64
    fork                                    # 448 + 2
    bz      r0                              # 450 + 3
f3_e:

    ll      r0,     fback_s - f3_help       # 453 + 7
    b       r0                              # 460 + 3
f3_help:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop             # 463 + 27


#
#  Fork 4
# --------
#
f4_s:
    ll      r0,     f5_s - f4_e             # 490 + 7
    lc      r2,     -3                      # 497 + 5
    ll      r3,     0xfe7e                  # 502 + 7
    str     [r2],   r3                      # 509 + 3
    # Aligned on 8 * 64
    fork                                    # 512 + 2
    bz      r0                              # 514 + 3
f4_e:

    ll      r0,     ffor_s - f4_help        # 517 + 7
    b       r0                              # 524 + 3
f4_help:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop                             # 527 + 43


#
#  Starter forward
# -----------------
#

st_f_s:
    mode    rocket                          # 570 + 3
    str     [r2],   r4                      # 573 + 3
    # Align on 9 * 64
    str     [r3],   r14                     # 576 + 3
    b       r0                              # 579 + 3
st_f_e:


#
#  Init forward
# --------------
#
#  registers:
#     r0:  dst jmp
#     r1:
#     r2:  dst write instr 1
#     r3:  dst write instr 2
#     r4:  instr 1 (commun): 'str [r2] r4; str'
#     r5:  instr 2: '[r3] r6; b ( r0 )'
#     r6:  instr 2: '[r3] r7; b ( r0 )'
#     r7:  instr 2: ...
#     r8:  instr 2
#     r9:  instr 2
#     r10: instr 2
#     r11: instr 2
#     r12:
#     r13: instr 2
#     r14: instr 2
#     r15: instr 2
#

for_s:
    mode    auricom                         # 582 + 3
    ll      r0,     384 - 9                 # 585 + 7
    ll      r2,     384 - 3                 # 592 + 7
    ll      r3,     384 - 2                 # 599 + 7

    # ll      r12,    0x0000

    ll      r4,     0xe42e                  # 606 + 7
    ll      r5,     0x7f63                  # 613 + 7
    ll      r6,     0x7f73                  # 620 + 7
    ll      r7,     0x7f83                  # 627 + 7
    ll      r8,     0x7f93                  # 634 + 7
    ll      r9,     0x7fa3                  # 641 + 7
    ll      r10,    0x7fb3                  # 648 + 7
    ll      r11,    0x7fd3                  # 655 + 7
    ll      r13,    0x7fe3                  # 662 + 7
    ll      r14,    0x7ff3                  # 669 + 7
    ll      r15,    0x7f53                  # 676 + 7

    ll      r1,     st_f_s - for_e          # 683 + 7
    b       r1                              # 690 + 3
for_e:


#
#  Init fast forward
# -------------------
#
#  registers:
#     r0:  dst jmp
#     r1:
#     r2:
#     r3:  dst write instr
#     r4:
#     r5:  instr 1: 'b r4; b'
#     r6:  instr 1
#     r7:  instr 1
#     r8:  instr 1
#     r9:  instr 1
#     r10: instr 1
#     r11: instr 1
#     r12: dst write instr (= r3)
#     r13: instr 1
#     r14: instr 1
#     r15: instr 2: 'check, nop, b'
#

ffor_s:
    ll      r0,     384 - 6                 # 693 + 7
    lc      r3,     -3                      # 700 + 5
    mov     r12,    r3                      # 705 + 3

    ll      r5,     0xf47f                  # 708 + 7
    mov     r6,     r5                      # 715 + 3
    mov     r7,     r5                      # 718 + 3
    mov     r8,     r5                      # 721 + 3
    mov     r9,     r5                      # 724 + 3
    mov     r10,    r5                      # 727 + 3
    mov     r11,    r5                      # 730 + 3
    mov     r13,    r5                      # 733 + 3
    mov     r14,    r5                      # 736 + 3
    ll      r15,    0xf1cf                  # 739 + 7
    mov     r1,     r15                     # 746 + 3

    # Timing
    ldb     [r0],   0,      62              # 749 + 7
    asr     r0,     0                       # 756 + 3
    mov     r0,     r0                      # 759 + 3

    nop                                     # 762 + 1

    ll      r2,     st_f_s - ffor_e + 6     # 763 + 7
    mode    rocket                          # 770 + 3
    b       r2                              # 773 + 3
ffor_e:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop                         # 776 + 34


#
#  Fork 5
# --------
#
f5_s:
    ll      r0,     lcleaner_s - f5_e       # 810 + 7
    lc      r2,     -3                      # 817 + 5
    ll      r3,     0xfe7e                  # 822 + 7
    str     [r2],   r3                      # 829 + 3
    # Aligned on 13 * 64
    fork                                    # 832 + 2
    bz      r0                              # 834 + 3
f5_e:


#
#  Init Main Ship
# ----------------
#
#  registers:
#     r0:  dst jmp after check
#     r1:  dst small jmp
#     r2:
#     r3:
#     r4:  dst jmp
#     r5:
#     r6:
#     r7:
#     r8:
#     r9:
#     r10:
#     r11:
#     r12:
#     r13:
#     r14:
#     r15:
#

ship_s:
    mode    auricom                         # 837 + 3
    ll      r0,     384 - 6                 # 840 + 7
    ll      r1,     256 - 6                 # 847 + 7
    ll      r4,     384 - 3                 # 854 + 7

    # Timing
    ldb     [r0],   0,      0xd1            # 861 + 7
    # write   r0
    # or      r15,    r15
    # or      r15,    r15

    nop nop nop nop nop nop nop nop         # 868 + 8

    ll      r5,    st_f_s - ship_e + 6      # 876 + 7
    mode    qirex                           # 883 + 3
    b       r5                              # 886 + 3
ship_e:


    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop nop nop
    nop nop nop nop nop nop nop nop         # 889 + 68

    str     [r2],   r4                      # 957 + 3
    # Aligned on 14 * 64
    str     [r3],   r15                     # 960 + 3
    b       r0                              # 963 + 3

    nop nop nop nop nop nop nop nop nop     # 966 + 9


lcleaner_s:
    mode    auricom                         # 975 + 3
    ll      r0,     0xf47f                  # 978 + 7
    ll      r1,     960 - lcleaner_w1_e     # 985 + 7
    ll      r2,     4800 - lcleaner_w2_e    # 992 + 7
    ll      r3,     12480 - lcleaner_w3_e   # 999 + 7
    ll      r4,     16320 - lcleaner_e      # 1006 + 7

    mode    miniplasma                      # 1013 + 3
    str     [r1],   r0                      # 1016 + 3
lcleaner_w1_e:
    str     [r2],   r0                      # 1019 + 3
lcleaner_w2_e:
    str     [r3],   r0                      # 1022 + 3
lcleaner_w3_e:
    str     [r4],   r0                      # 1025 + 3
lcleaner_e:
